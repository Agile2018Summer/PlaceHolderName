package edu.harvard.integration;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class JSONHandler {
    public static Object Json2Map(String json){
        JsonArray arr = Jsoner.deserialize(json, new JsonArray());
        if(!arr.isEmpty()) return arr;
        return Jsoner.deserialize(json, new JsonObject());
    }

    public static String Map2Json(JsonObject map) {
        return map.toJson();
    }

    public static String List2Json(JsonArray arr) {
        return arr.toJson();
    }
}
