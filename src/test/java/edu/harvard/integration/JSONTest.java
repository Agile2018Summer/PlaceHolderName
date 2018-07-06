package edu.harvard.integration;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import edu.harvard.integration.JSONHandler;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JSONTest {
    @Test
    public void testConvert(){
        String input = "{\"a\":\"123\",\"b\":\"1234\"}";
        JsonObject obj = (JsonObject) JSONHandler.Json2Map(input);
        System.out.println(obj);
        assertEquals(input, JSONHandler.Map2Json(obj));
    }

    @Test
    public void testConvertList(){
        String input = "[{\"a\":\"123\"},{\"b\":\"1234\"}]";
        JsonArray arr = (JsonArray) JSONHandler.Json2Map(input);
        System.out.println(arr);
        assertEquals(input, JSONHandler.List2Json(arr));
    }
}
