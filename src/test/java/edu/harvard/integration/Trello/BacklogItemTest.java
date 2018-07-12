package edu.harvard.integration.Trello;

import org.junit.Test;

import java.util.Calendar;
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

    @Test
    public void testDateConvert(){
        Map<String, Object> map = new HashMap<>();
        map.put("pts", "1");
        map.put("name", "asdfasdf");
        map.put("due", "2018-07-05T16:00:00.000Z");
        map.put("list_name", "Done this semester");
        BacklogItem item = new BacklogItem(map);
        Calendar c = item.getDateCompleted();
        assertEquals(c.get(Calendar.YEAR), 2018);
        assertEquals(c.get(Calendar.MONTH), 6);
        assertEquals(c.get(Calendar.DATE), 5);
        map.put("due", "2018-12-22T16:00:00.000Z");
        item = new BacklogItem(map);
        c = item.getDateCompleted();
        assertEquals(c.get(Calendar.YEAR), 2018);
        assertEquals(c.get(Calendar.MONTH), 11);
        assertEquals(c.get(Calendar.DATE), 22);
    }

}
