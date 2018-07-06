package edu.harvard.integration;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class JSONHandler {
    public static Object Json2Map(String json){
        return Jsoner.deserialize(json, new JsonObject());
        /*try {
            JSONParser parser = new JSONParser();
            return parser.parse(json);
        } catch (ParseException e) {
            System.err.println("(ERROR) Parsing JSON String error. String: "
                    + json);
            return null;
        }*/
    }

    public static String Map2Json(JsonObject map) {
        return map.toJson();
    }

    public static String List2Json(JsonArray arr) {
        return arr.toJson();
    }
}
