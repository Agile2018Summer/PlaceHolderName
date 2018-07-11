package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import edu.harvard.integration.JSONHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrelloConnection {
    private String token = "";
    private String key = "";

    TrelloConnection(String token, String key) {
        this.token = token;
        this.key = key;
    }

    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);

            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            conn.connect();

            Map<String, List<String>> map = conn.getHeaderFields();

            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }
        } catch (Exception e) {
            System.out.println("Get request sent exception." + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    List<Map<String, String>> trackAllBoards(){
        List<Map<String, String>> result = new ArrayList<>();
        String url = "https://api.trello.com/1/members/me/boards?key=" + key +
                "&token=" + token;
        String info = Commons.getPageSource(url);
        JsonArray maps = (JsonArray) JSONHandler.Json2Map(info);
        for (Object map : maps) {
            Map<String, String> m = new HashMap<>();
            m.put("id", ((Map<String, String>) map).get("id"));
            m.put("name", ((Map<String, String>) map).get("name"));
            result.add(m);
        }
        return result;
    }
}
