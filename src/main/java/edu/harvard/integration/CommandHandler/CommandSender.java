package edu.harvard.integration.CommandHandler;

import edu.harvard.integration.Integrator;
import edu.harvard.integration.Slack.SlackUser;

import javax.annotation.Nonnull;

/**
 * Stores general information about who sent a {@link edu.harvard.integration.api.Cmd}.
 */
public final class CommandSender {
    private final CommandSource source;
    private SlackUser slackUser;
    private String channel;
    private boolean isPrivate;

    /**
     * Creates a Console instance of {@link CommandSender}.
     */
    public CommandSender() { //TODO: Decide if the console command sender should be static
        this.source = CommandSource.Console;
    }

    /**
     * Creates a Slack instance of {@link CommandSender}.
     * @param user    The {@link SlackUser} behind the {@link CommandSender}.
     * @param channel The ID of the Slack channel the {@link SlackUser} is in.
     */
    public CommandSender(SlackUser user, String channel) {
        this.source = CommandSource.Slack;
        this.slackUser = user;
        this.channel = channel;
        this.isPrivate = this.channel.startsWith("D");
    }

    /**
     * Retrieves the {@link CommandSource} of {@link CommandSender} object.
     * @return The {@link CommandSource} of {@link CommandSender} object.
     */
    public CommandSource getSource() {
        return this.source;
    }

    /**
     * If the {@link CommandSource} is {@link CommandSource#Slack} then it returns the {@link SlackUser} behind this {@link CommandSender}.
     * @return The {@link SlackUser} behind this {@link CommandSender}.
     */
    public SlackUser getSlackUser() {
        return this.slackUser;
    }

    /**
     * Gets if the {@link CommandSender} is in a direct message with {@link Integrator}.
     * @return True if the message was a private message to {@link Integrator}, false otherwise.
     */
    public boolean isPrivate() {
        return this.isPrivate;
    }

    /**
     * Gets the Channel ID of the Slack channel the message was sent in.
     * @return The Slack Channel ID.
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Sends a message to the {@link CommandSender}.
     * @param message The message to send.
     */
    public void sendMessage(@Nonnull String message) {
        switch (this.source) {
            case Slack:
                Integrator.getSlack().sendMessage(message, this.channel);
                break;
            case Console:
                Integrator.getLogger().info(message);
                break;
            default:
                break;
        }
    }
}