package edu.harvard.integration.Trello;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static edu.harvard.integration.Trello.BacklogColumn.BACKLOG;
import static edu.harvard.integration.Trello.BacklogColumn.DONE;
import static edu.harvard.integration.Trello.BacklogColumn.IN_PROGRESS;
import static org.junit.Assert.assertEquals;

public class BacklogItemTest {
    @Test
    public void testConvertStatus(){
        Map<String, Object> map = new HashMap<>();
        map.put("pts", "1");
        map.put("name", "asdfasdf");
        map.put("due", "2018-07-05T16:00:00.000Z");
        map.put("list_name", "Done this semester");
        BacklogItem item = new BacklogItem(map);
        assertEquals(DONE, item.getColumn());
        map.put("list_name", "In progress");
        item = new BacklogItem(map);
        assertEquals(IN_PROGRESS, item.getColumn());
        map.put("list_name", "Today's class");
        item = new BacklogItem(map);
        assertEquals(BACKLOG, item.getColumn());
    }
}
