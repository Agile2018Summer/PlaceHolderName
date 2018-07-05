package edu.harvard.integration.Trello;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commons {
    public static List<Map<String, Object>> getInfo(JSONArray arr){
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
}
