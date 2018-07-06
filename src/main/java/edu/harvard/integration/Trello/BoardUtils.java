package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import edu.harvard.integration.JSONHandler;

import java.util.List;
import java.util.Map;

public class BoardUtils {
    public static List<Map<String, Object>> getBoardContent(String id) {
        String url = "https://api.trello.com/1/boards/" + id + "/cards";
        String resp = Commons.getPageSource(url);
        JsonArray list = (JsonArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }

    public static List<Map<String, Object>> getBoardLists(String id){
        String url = "https://api.trello.com/1/boards/" + id + "/lists";
        String resp = Commons.getPageSource(url);
        JsonArray list = (JsonArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }
}
