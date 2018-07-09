package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.JSONHandler;

import java.util.List;
import java.util.Map;

public class ListUtils {
    public static String getListIdByName(String boardId, String listName){
        List<Map<String, Object>> lists = BoardUtils.getBoardLists(boardId);
        if(lists == null || lists.size() == 0){
            return null;
        }
        for(Map<String, Object> list : lists){
            if(list.get("name").equals(listName)){
                return (String) list.get("id");
            }
        }
        return null;
    }

    public static List<Map<String, Object>> getListContent(String id) {
        String url = "https://api.trello.com/1/lists/" + id + "/cards";
        String resp = Commons.getPageSource(url);
        JsonArray list = (JsonArray) JSONHandler.Json2Map(resp);
        List<Map<String, Object>> res = Commons.getInfo(list);
        return res;
    }
}
