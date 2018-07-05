package edu.harvard.integration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHandler {
    public static Object Json2Map(String json){
        try {
            JSONParser parser = new JSONParser();
            return parser.parse(json);
        } catch (ParseException e) {
            System.err.println("(ERROR) Parsing JSON String error. String: "
                    + json);
            return null;
        }
    }

    public static String Map2Json(JSONObject map) {
        return map.toJSONString();
    }

    public static String List2Json(JSONArray arr) {
        return arr.toJSONString();
    }
}
