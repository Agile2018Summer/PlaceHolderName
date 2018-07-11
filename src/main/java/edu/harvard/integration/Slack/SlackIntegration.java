package edu.harvard.integration.Slack;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.neovisionaries.ws.client.*;
import edu.harvard.integration.CommandHandler.CommandSender;
import edu.harvard.integration.Config;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.api.Integration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of {@link edu.harvard.integration.api.Integration} to connect to the Slack server.
 */
public class SlackIntegration implements Integration {
    private final Map<String, SlackUser> userMap = new HashMap<>();
    private boolean isConnected;
    private final String token;
    private final String infoChannel;//TODO: maybe convert it to an array
    private WebSocket ws;
    private int id;

    public SlackIntegration() {
        Config config = Integrator.getConfig();
        this.token = config.getOrDefault("SLACK_TOKEN", "token");
        this.infoChannel = config.getOrDefault("INFO_CHANNEL", "info_channel");
        if (this.token.equals("token") || this.infoChannel.equals("info_channel")) {
            Integrator.getLogger().error("Failed to load needed configs for Slack Integration");
            return;
        }
        if (connect())
            Integrator.getLogger().info("Connected to slack.");
        else
            Integrator.getLogger().error("Failed to connect to slack.");
    }

    /**
     * Decodes the given slack message.
     * @param text The message to decode.
     * @return The clean readable message.
     */
    @Nonnull
    private String cleanChat(String text) {
        if (text == null || text.isEmpty())
            return "";
        //Users
        Matcher matcher = Pattern.compile("<@(.*?)>").matcher(text);
        while (matcher.find()) {
            String str = matcher.group(1);
            SlackUser user = getUserInfo(str);
            text = text.replace("<@" + str + '>', '@' + (user == null ? "null" : user.getName()));
        }
        //Channel
        matcher = Pattern.compile("<#(.*?\\|.*?)>").matcher(text);
        while (matcher.find()) {
            String str = matcher.group(1);
            text = text.replace("<#" + str + '>', '#' + str.split("\\|")[1]);
        }

        //URLS with http or https
        matcher = Pattern.compile("<(http[^|;]+)>").matcher(text);
        while (matcher.find()) {
            String str = matcher.group(1);
            text = text.replace('<' + str + '>', str);
        }

        //Date, email address, Remaining URLs
        matcher = Pattern.compile("<(.*?\\|.*?)>").matcher(text);
        while (matcher.find()) {
            String str = matcher.group(1);
            text = text.replace('<' + str + '>', str.split("\\|")[1]);
        }
        return text.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    public void stop() {
        if (!isConnected)
            return;
        userMap.clear();
        sendMessage("Disconnected.", infoChannel);
        isConnected = false;
        if (ws != null)
            ws.disconnect();
    }

    /**
     * Connects to the Slack server.
     * @return True if this {@link edu.harvard.integration.api.Integration} connected successfully, false otherwise.
     */
    private boolean connect() {
        if (isConnected)
            return false;
        try {
            URL url = new URL("https://slack.com/api/rtm.connect?token=" + token);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
            }
            JsonObject json = Jsoner.deserialize(response.toString(), new JsonObject());
            String webSocketUrl = json.getString(Jsoner.mintJsonKey("url", null));
            if (webSocketUrl != null)
                openWebSocket(webSocketUrl);
        } catch (IOException ignored) {
        }
        isConnected = true;
        sendMessage("Connected.", infoChannel);
        return true;
    }

    /**
     * Opens a {@link WebSocket} to the specified url.
     * @param url The url of the {@link WebSocket}.
     */
    private void openWebSocket(@Nonnull String url) {
        try {
            ws = new WebSocketFactory().createSocket(url).addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {//TODO listen back for potential replies to stored message ids
                    JsonObject json = Jsoner.deserialize(message, new JsonObject());
                    if (json.containsKey("type")) {
                        if (json.getString(Jsoner.mintJsonKey("type", null)).equals("message")) {
                            if (json.containsKey("bot_id"))
                                return;
                            SlackUser info = getUserInfo(json.getString(Jsoner.mintJsonKey("user", null)));
                            if (info == null)
                                return;
                            String channel = json.getString(Jsoner.mintJsonKey("channel", null));
                            sendSlackChat(info, cleanChat(json.getString(Jsoner.mintJsonKey("text", null))), channel);
                        }
                    }
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                    if (isConnected) {
                        Integrator.getSlack().stop();
                        Integrator.getSlack().connect();
                    }
                }
            }).connect();
        } catch (WebSocketException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the ID of the channel that this {@link edu.harvard.integration.api.Integration} sends messages to.
     * @return The ID of the channel to send messages to.
     */
    @Nonnull
    public String getInfoChannel() {
        return this.infoChannel;
    }

    /**
     * Sends the given message to the given Slack channel.
     * @param message The message to send.
     * @param channel The ID of the Slack channel to send the message to.
     */
    public void sendMessage(@Nonnull String message, @Nonnull String channel) {
        if (message.endsWith("\n"))
            message = message.substring(0, message.length() - 1);
        JsonObject json = new JsonObject();
        json.put("id", this.id++);
        json.put("type", "message");
        json.put("channel", channel);
        json.put("text", message);
        ws.sendText(Jsoner.serialize(json));
    }

    public void sendFile(File file) {
        if (!file.exists()) {
            return;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("https://slack.com/api/files.upload?token=" + token + "&channels=" + infoChannel);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            builder.addBinaryBody("file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            response.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a {@link SlackUser} by their Slack ID.
     * @param id The ID of the {@link SlackUser} to get.
     * @return The {@link SlackUser} with the given ID, or null if no {@link SlackUser} was found with the given ID.
     */
    @Nullable
    private SlackUser getUserInfo(String id) {
        SlackUser user = userMap.get(id);
        if (user != null)
            return user;
        try {
            URL url = new URL("https://slack.com/api/users.info?token=" + token + "&user=" + id);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JsonObject jsonResponse = Jsoner.deserialize(response.toString(), new JsonObject());
            if (!jsonResponse.getBooleanOrDefault(Jsoner.mintJsonKey("ok", false)))
                return null; //User does not exist or is deactivated
            JsonObject userInfo = (JsonObject) jsonResponse.get("user");
            if (userInfo.getBooleanOrDefault(Jsoner.mintJsonKey("deleted", false))) //This may not ever even be true, given account is deactivated
                return null;
            userMap.put(id, user = new SlackUser(userInfo));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Handles incoming chat from Slack.
     * @param info    The {@link SlackUser} who sent the message.
     * @param message The message the {@link SlackUser} sent.
     * @param channel The ID of the channel the message was sent in.
     */
    private void sendSlackChat(SlackUser info, @Nonnull String message, @Nonnull String channel) {
        if (info == null)
            return;
        if (info.isBot()) //If bot don't send, we should process it but not ever as a command
            return;
        boolean isCommand = false;
        CommandSender sender = new CommandSender(info, channel);
        if (message.startsWith("!"))
            isCommand = Integrator.getCommandHandler().handleCommand(message, sender);
        if (isCommand)
            return;
        //if (!channel.startsWith("D")) {//If not pm, but should it also check private messages?

        //}//TODO: Maybe add some functionality if it is a pm
    }
}