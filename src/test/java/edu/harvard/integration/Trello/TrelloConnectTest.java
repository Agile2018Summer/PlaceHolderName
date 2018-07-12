package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import edu.harvard.integration.JSONHandler;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TrelloConnectTest {
    private String key = "44acea80fcb43f99bd8950c20477d1a8";
    private String token = "6ef2801bd68fa76e8a16d31b51388b99fc5d581e271f25dba89b156a599a229b";

    @Test
    public void test0(){
        assertEquals(3, 3);
    }

    @Test
    public void testConnection(){
        String url = "https://api.trello.com/1/members/me/boards?key=" + key +
                "&token=" + token;
        String res = Commons.getPageSource(url);
        System.out.println(res);
    }

    @Test
    public void getUserBoardsTest(){
        String url = "https://api.trello.com/1/members/me/boards?key=" + key +
                "&token=" + token;
        String res = Commons.getPageSource(url);
        JsonArray maps = (JsonArray) JSONHandler.Json2Map(res);
        for(Object map : maps){
            System.out.println(map);
        }
    }

    @Test
    public void getBoardContentTest(){
        String id = "58cec57600cd9fee6e35bad9";
        List<Map<String, Object>> res = BoardUtils.getBoardContent(id);
        for(Map<String, Object> map : res){
            System.out.println(map);
        }
    }

    @Test
    public void getBoardListsTest(){
        String id = "58cec57600cd9fee6e35bad9";
        List<Map<String, Object>> res = BoardUtils.getBoardLists(id);
        for(Map<String, Object> map : res){
            System.out.println(map);
        }
    }

    @Test
    public void getCardContentTest(){
        String id = "58cec57700cd9fee6e35baf7";
        BacklogItem res = CardUtils.getCardContent(id);
        System.out.println(res.toString());
    }

    @Test
    public void geListContentTest(){
        String id = "58cec57600cd9fee6e35bada";
        List<Map<String, Object>> res = ListUtils.getListContent(id);
        for(Map<String, Object> map : res){
            System.out.println(map);
        }
    }

    @Test
    public void testTrackTrelloBoards(){
        TrelloConnection c = new TrelloConnection(token, key);
        List<Map<String, String>> list = c.trackAllBoards();
        for(Map<String, String> m : list) System.out.println(m);
    }

    @Test
    public void getTrelloInfoByListTest(){
        TrelloIntegration g = new TrelloIntegration(key, token);
        List<Map<String, Object>> infoList
                = g.getInfoByList("Harvard CSCI S-71 Course Backlog", "Course backlog");
        for(Map<String, Object> m : infoList) System.out.println(m);
    }

    @Test
    public void testGetAllPBIs(){
        TrelloIntegration g = new TrelloIntegration(key, token);
        List<BacklogItem> res = g.getAllPBIs("Harvard CSCI S-71 Course Backlog", token, key);
        System.out.println(res.get(0).toString());
        assertEquals(res.size(), 24);
    }

    @Test
    public void testGetInfo(){
        JsonObject test = (JsonObject) JSONHandler.Json2Map("{\"name\":\"(1) Retrospectives\", " +
                "\"id\":\"58cec57700cd9fee6e35baf2\"}");
        JsonArray jarr = new JsonArray();
        jarr.add(test);
        List<Map<String, Object>> arr = Commons.getInfo(jarr);
        assertEquals("1", (String) (arr.get(0).get("pts")));
        assertEquals("Retrospectives", (String) (arr.get(0).get("name")));
    }

    @Test
    public void testGetInfo2(){
        JsonObject test = (JsonObject) JSONHandler.Json2Map(
                "{\"name\":\"(2) Continuous Integration, Continuous Delivery, and DevOps\", " +
                "\"id\":\"58cec57700cd9fee6e35baf2\"}");
        JsonArray jarr = new JsonArray();
        jarr.add(test);
        List<Map<String, Object>> arr = Commons.getInfo(jarr);
        assertEquals("2", (String) (arr.get(0).get("pts")));
        assertEquals("Continuous Integration, Continuous Delivery, and DevOps",
                (String) (arr.get(0).get("name")));
    }

    @Test
    public void testGetListNameByCard(){
        String cardId = "58cec57700cd9fee6e35baf7";
        String listName = CardUtils.getListNameByCard(cardId);
        assertEquals(listName, "Done this semester");
    }

    @Test
    public void testGetBoardIdByName(){
        String boardId =
                BoardUtils.getBoardIdByName(token, key,
                        "Harvard CSCI S-71 Course Backlog");
        assertEquals("58cec57600cd9fee6e35bad9", boardId);
    }
}
