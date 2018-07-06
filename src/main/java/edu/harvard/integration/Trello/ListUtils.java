package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import edu.harvard.integration.JSONHandler;

import java.util.List;
import java.util.Map;

public class ListUtils {
    public static List<Map<String, Object>> getListContent(String id) {
        TrelloConnection c = new TrelloConnection();
        String url = "https://api.trello.com/1/lists/" + id + "/cards";
        String resp = c.getPageSource(url);
        JsonArray list = (JsonArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }
}
