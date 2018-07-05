package edu.harvard.integration.Trello;

import edu.harvard.integration.JSONHandler;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils {
    public static List<Map<String, Object>> getBoardContent(String id) {
        TrelloConnection c = new TrelloConnection();
        String url = "https://api.trello.com/1/boards/" + id + "/cards";
        String resp = c.getPageSource(url);
        JSONArray list = (JSONArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }

    public static List<Map<String, Object>> getBoardLists(String id){
        TrelloConnection c = new TrelloConnection();
        String url = "https://api.trello.com/1/boards/" + id + "/lists";
        String resp = c.getPageSource(url);
        JSONArray list = (JSONArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }
}
