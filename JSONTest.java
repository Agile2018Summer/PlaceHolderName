package edu.harvard.integration;

import edu.harvard.integration.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JSONTest {
    @Test
    public void testConvert(){
        String input = "{\"a\":\"123\",\"b\":\"1234\"}";
        JSONObject obj = (JSONObject) JSONHandler.Json2Map(input);
        System.out.println(obj);
        assertEquals(input, JSONHandler.Map2Json(obj));
    }

    @Test
    public void testConvertList(){
        String input = "[{\"a\":\"123\"},{\"b\":\"1234\"}]";
        JSONArray arr = (JSONArray) JSONHandler.Json2Map(input);
        System.out.println(arr);
        assertEquals(input, JSONHandler.List2Json(arr));
    }
}
