package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {
    public static List<Map<String, Object>> getInfo(JsonArray arr){
        Pattern pattern = Pattern.compile("^\\([0-9]+\\)");
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (Object aList : arr) {
            Map<String, Object> item = (Map<String, Object>) aList;
            Map<String, Object> map = new HashMap<String, Object>();
            String name = (String) item.get("name");
            Matcher matcher = pattern.matcher(name);
            String pts = "";
            if(matcher.find()) pts = matcher.group();
            pts = pts.replace("(", "");
            pts = pts.replace(")", "");
            name = name.replaceFirst("^\\([0-9]+\\)\\s+", "");
            map.put("pts", pts);
            map.put("name", name);
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
