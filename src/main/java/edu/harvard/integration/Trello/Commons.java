package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;

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
}
