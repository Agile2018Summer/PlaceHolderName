package edu.harvard.integration;

import edu.harvard.integration.Trello.BacklogColumn;
import edu.harvard.integration.Trello.BacklogItem;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class ChartHelperTest {
    @Test
    public void parseTest() {
        Set<BacklogItem> items = new HashSet<>();
        items.add(new BacklogItem("Intro to the course", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Introduction to Agile", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Scrum", Utils.getDate("6/27/2018"), 3, BacklogColumn.DONE));
        items.add(new BacklogItem("Product inception", Utils.getDate("6/27/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile requirements", Utils.getDate("6/28/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Definition of Done, Definition of Ready", Utils.getDate("6/28/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Estimating", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile forecasting and project management", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Sprint Planning", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile technical skills: Pair Programming", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("TDD with Mob Programming", Utils.getDate("7/3/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Clean code, refactoring, and legacy code with Mob Programming", 3, BacklogColumn.IN_PROGRESS));
        items.add(new BacklogItem("Continuous Integration, Continuous Delivery, and DevOps", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Retrospectives", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("High-performance teams: Core Protocols for psychological safety and EI", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Behavior Driven Development (BDD) and Acceptance Test-Driven Development (A-TDD)", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Agile at large scale", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Introducing and sustaining Agile in your organization", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Final project presentation", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Wrap up", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Ask me anything", 1, BacklogColumn.BACKLOG));

        List<Calendar> backlogDates = Arrays.asList(
                new Calendar.Builder().setDate(2018, 5, 25).build(),
                new Calendar.Builder().setDate(2018, 5, 26).build(),
                new Calendar.Builder().setDate(2018, 5, 27).build(),
                new Calendar.Builder().setDate(2018, 5, 28).build(),
                new Calendar.Builder().setDate(2018, 6, 2).build(),
                new Calendar.Builder().setDate(2018, 6, 3).build(),
                new Calendar.Builder().setDate(2018, 6, 5).build(),
                new Calendar.Builder().setDate(2018, 6, 9).build(),
                new Calendar.Builder().setDate(2018, 6, 10).build(),
                new Calendar.Builder().setDate(2018, 6, 11).build(),
                new Calendar.Builder().setDate(2018, 6, 12).build()
        );

        List<ChartHelper.ColumnInfo> productInfos = ChartHelper.parseItems(items, backlogDates);
        assertEquals("6/25/2018", productInfos.get(0).getLabel());
        assertEquals(29, productInfos.get(0).getRemainingPoints());
        assertEquals(29, productInfos.get(1).getRemainingPoints());
        assertEquals(24, productInfos.get(2).getRemainingPoints());
    }

    @Test
    public void pointCountTest() {
        Set<BacklogItem> items = new HashSet<>();
        items.add(new BacklogItem("Intro to the course", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Introduction to Agile", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Scrum", Utils.getDate("6/26/2018"), 3, BacklogColumn.DONE));
        items.add(new BacklogItem("Product inception", Utils.getDate("6/27/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile requirements", Utils.getDate("6/28/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Definition of Done, Definition of Ready", Utils.getDate("6/28/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Estimating", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile forecasting and project management", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Sprint Planning", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile technical skills: Pair Programming", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("TDD with Mob Programming", Utils.getDate("7/3/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Clean code, refactoring, and legacy code with Mob Programming", 3, BacklogColumn.IN_PROGRESS));
        items.add(new BacklogItem("Continuous Integration, Continuous Delivery, and DevOps", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Retrospectives", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("High-performance teams: Core Protocols for psychological safety and EI", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Behavior Driven Development (BDD) and Acceptance Test-Driven Development (A-TDD)", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Agile at large scale", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Introducing and sustaining Agile in your organization", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Final project presentation", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Wrap up", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Ask me anything", 1, BacklogColumn.BACKLOG));

        assertEquals(31, ChartHelper.getTotalPoints(items));
    }

    @Test
    public void testImageCreate() {
        Set<BacklogItem> items = new HashSet<>();
        items.add(new BacklogItem("Intro to the course", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Introduction to Agile", Utils.getDate("6/25/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Scrum", Utils.getDate("6/27/2018"), 3, BacklogColumn.DONE));
        items.add(new BacklogItem("Product inception", Utils.getDate("6/27/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile requirements", Utils.getDate("6/28/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Definition of Done, Definition of Ready", Utils.getDate("6/28/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Estimating", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile forecasting and project management", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Sprint Planning", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("Agile technical skills: Pair Programming", Utils.getDate("7/2/2018"), 1, BacklogColumn.DONE));
        items.add(new BacklogItem("TDD with Mob Programming", Utils.getDate("7/3/2018"), 2, BacklogColumn.DONE));
        items.add(new BacklogItem("Clean code, refactoring, and legacy code with Mob Programming", 3, BacklogColumn.IN_PROGRESS));
        items.add(new BacklogItem("Continuous Integration, Continuous Delivery, and DevOps", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Retrospectives", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("High-performance teams: Core Protocols for psychological safety and EI", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Behavior Driven Development (BDD) and Acceptance Test-Driven Development (A-TDD)", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Agile at large scale", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Introducing and sustaining Agile in your organization", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Final project presentation", 2, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Wrap up", 1, BacklogColumn.BACKLOG));
        items.add(new BacklogItem("Ask me anything", 1, BacklogColumn.BACKLOG));

        List<Calendar> backlogDates = Arrays.asList(
                new Calendar.Builder().setDate(2018, 5, 25).build(),
                new Calendar.Builder().setDate(2018, 5, 26).build(),
                new Calendar.Builder().setDate(2018, 5, 27).build(),
                new Calendar.Builder().setDate(2018, 5, 28).build(),
                new Calendar.Builder().setDate(2018, 6, 2).build(),
                new Calendar.Builder().setDate(2018, 6, 3).build(),
                new Calendar.Builder().setDate(2018, 6, 5).build(),
                new Calendar.Builder().setDate(2018, 6, 9).build(),
                new Calendar.Builder().setDate(2018, 6, 10).build(),
                new Calendar.Builder().setDate(2018, 6, 11).build(),
                new Calendar.Builder().setDate(2018, 6, 12).build()
        );

        ChartHelper.setData(items, backlogDates);
        //TODO: Uncomment this below and figure out how to compare the image a test image
        //Application.launch(ChartHelper.class);
    }
}