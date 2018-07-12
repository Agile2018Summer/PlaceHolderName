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
        String res = "[{\"name\":\"Harvard CSCI S-71 Course Backlog\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"limits\":null,\"pinned\":null,\"invitations\":null,\"shortLink\":\"sQfxo5KK\",\"powerUps\":[],\"dateLastActivity\":\"2018-07-11T13:20:29.509Z\",\"idTags\":[],\"datePluginDisable\":null,\"id\":\"58cec57600cd9fee6e35bad9\",\"invited\":false,\"starred\":true,\"url\":\"https://trello.com/b/sQfxo5KK/harvard-csci-s-71-course-backlog\",\"prefs\":{\"permissionLevel\":\"public\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":false,\"cardCovers\":true,\"cardAging\":\"regular\",\"calendarFeedEnabled\":false,\"background\":\"574da6af6f1c6af490a33ccf\",\"backgroundImage\":\"https://trello-backgrounds.s3.amazonaws.com/5433fa84a75436fe6d4ef388/500x300/c0dd02fc3f5a9a7dead8b1d7924e2a80/usa_harvard.jpg\",\"backgroundImageScaled\":[{\"width\":140,\"height\":100,\"url\":\"https://trello-backgrounds.s3.amazonaws.com/5433fa84a75436fe6d4ef388/140x100/cbfc66813cbea4192994a33b42033dde/usa_harvard.jpg\"},{\"width\":256,\"height\":192,\"url\":\"https://trello-backgrounds.s3.amazonaws.com/5433fa84a75436fe6d4ef388/256x192/eda1d7dee52e3ba06216cf36f251b14b/usa_harvard.jpg\"},{\"width\":480,\"height\":480,\"url\":\"https://trello-backgrounds.s3.amazonaws.com/5433fa84a75436fe6d4ef388/480x480/7440d5c6ed4fc239fc5e6fde96c26a24/usa_harvard.jpg\"},{\"width\":500,\"height\":300,\"url\":\"https://trello-backgrounds.s3.amazonaws.com/5433fa84a75436fe6d4ef388/500x300/c0dd02fc3f5a9a7dead8b1d7924e2a80/usa_harvard.jpg\"}],\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundBottomColor\":\"#524941\",\"backgroundTopColor\":\"#7baee2\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"subscribed\":false,\"labelNames\":{\"green\":\"Possible bonus topic\",\"yellow\":\"Guest lecturer\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"},\"dateLastView\":\"2018-07-12T03:33:42.506Z\",\"shortUrl\":\"https://trello.com/b/sQfxo5KK\",\"memberships\":[{\"id\":\"58cec57700cd9fee6e35bb00\",\"idMember\":\"5433fa84a75436fe6d4ef388\",\"memberType\":\"admin\",\"unconfirmed\":false}]},{\"name\":\"How to Use Trello for iPhone\",\"desc\":\"\",\"descData\":null,\"closed\":false,\"idOrganization\":null,\"limits\":null,\"pinned\":null,\"invitations\":null,\"shortLink\":\"nk0HKD4s\",\"powerUps\":[],\"dateLastActivity\":\"2018-04-13T00:39:52.711Z\",\"idTags\":[],\"datePluginDisable\":null,\"id\":\"5acffc580314b2192643d8f2\",\"invited\":false,\"starred\":false,\"url\":\"https://trello.com/b/nk0HKD4s/how-to-use-trello-for-iphone\",\"prefs\":{\"permissionLevel\":\"private\",\"voting\":\"disabled\",\"comments\":\"members\",\"invitations\":\"members\",\"selfJoin\":true,\"cardCovers\":true,\"calendarFeedEnabled\":false,\"background\":\"blue\",\"backgroundImage\":null,\"backgroundImageScaled\":null,\"backgroundTile\":false,\"backgroundBrightness\":\"dark\",\"backgroundColor\":\"#0079BF\",\"backgroundBottomColor\":\"#0079BF\",\"backgroundTopColor\":\"#0079BF\",\"canBePublic\":true,\"canBeOrg\":true,\"canBePrivate\":true,\"canInvite\":true},\"subscribed\":false,\"labelNames\":{\"green\":\"\",\"yellow\":\"\",\"orange\":\"\",\"red\":\"\",\"purple\":\"\",\"blue\":\"\",\"sky\":\"\",\"lime\":\"\",\"pink\":\"\",\"black\":\"\"},\"dateLastView\":\"2018-04-13T00:40:08.146Z\",\"shortUrl\":\"https://trello.com/b/nk0HKD4s\",\"memberships\":[{\"id\":\"5acffc580314b2192643d8f6\",\"idMember\":\"4e6a7fad05d98b02ba00845c\",\"memberType\":\"normal\",\"unconfirmed\":false,\"deactivated\":false},{\"id\":\"5acffc580314b2192643d8f7\",\"idMember\":\"50095233f62adbe04d935195\",\"memberType\":\"normal\",\"unconfirmed\":false,\"deactivated\":false},{\"id\":\"5acffc580314b2192643d8f8\",\"idMember\":\"5acffc580314b2192643d8eb\",\"memberType\":\"admin\",\"unconfirmed\":false,\"deactivated\":false}]}]\n";
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

    @Test
    public void testGetAllTrelloBoards(){
        TrelloIntegration g = new TrelloIntegration(key, token);
        Map<String, Object> info = g.getAllTrelloBoards(token, key);
        System.out.println(info);
    }
}
