package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonObject;
import edu.harvard.integration.JSONHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardUtils {

    public static Map<String, Object> getCardContent(String id) {
        TrelloConnection c = new TrelloConnection();
        String url = "https://api.trello.com/1/cards/" + id + "?fields=all";
        String resp = c.getPageSource(url);
        JsonObject res = (JsonObject) JSONHandler.Json2Map(resp);
        return filterInfo(res);
    }

    private static Map<String, Object> filterInfo(Map<String, Object> cardItem){
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("name", cardItem.get("name"));
        res.put("desc", cardItem.get("desc"));
        res.put("due", cardItem.get("due"));
        res.put("labels", cardItem.get("labels"));
        return res;
    }
}
