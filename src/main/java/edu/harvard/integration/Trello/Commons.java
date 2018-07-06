package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commons {
    public static List<Map<String, Object>> getInfo(JsonArray arr){
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (Object aList : arr) {
            Map<String, Object> item = (Map<String, Object>) aList;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", item.get("name"));
            map.put("id", item.get("id"));
            res.add(map);
        }
        return res;
    }

    public static String getPageSource(String pageUrl) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(pageUrl);
            String encoding = "UTF-8";

            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream(), encoding));
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
