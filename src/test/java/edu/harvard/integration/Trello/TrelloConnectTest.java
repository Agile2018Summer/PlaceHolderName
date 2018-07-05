package edu.harvard.integration.Trello;

import edu.harvard.integration.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        TrelloConnection c = new TrelloConnection(token, key);
        String res = c.getPageSource(url);
        System.out.println(res);
    }

    @Test
    public void getBoardListTest(){
        String url = "https://api.trello.com/1/members/me/boards?key=" + key +
                "&token=" + token;
        TrelloConnection c = new TrelloConnection(token, key);
        String res = c.getPageSource(url);
        JSONArray map = (JSONArray) JSONHandler.Json2Map(res);
        System.out.println(map);
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
    public void getCardContentTest(){
        String id = "58cec57700cd9fee6e35baf7";
        Map<String, Object> res = CardUtils.getCardContent(id);
        System.out.println(res);
    }

}
