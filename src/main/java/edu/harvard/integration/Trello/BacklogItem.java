package edu.harvard.integration.Trello;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Map;

import static edu.harvard.integration.Trello.BacklogColumn.BACKLOG;
import static edu.harvard.integration.Trello.BacklogColumn.DONE;
import static edu.harvard.integration.Trello.BacklogColumn.IN_PROGRESS;


public class BacklogItem {
    private String title, description;
    private BacklogColumn column;
    private int storyPoints;
    private Calendar dateCompleted;

    public BacklogItem(@Nonnull String title, int storyPoints, @Nonnull BacklogColumn column) {
        this(title, null, storyPoints, column, null);
    }

    public BacklogItem(@Nonnull String title, @Nullable Calendar dateCompleted, int storyPoints, @Nonnull BacklogColumn column) {
        this(title, dateCompleted, storyPoints, column, null);
    }

    public BacklogItem(@Nonnull String title, @Nullable Calendar dateCompleted, int storyPoints, @Nonnull BacklogColumn column, @Nullable String description) {
        this.storyPoints = storyPoints;
        this.title = title;
        this.description = description;
        this.dateCompleted = dateCompleted;
        this.column = column;
    }

    public BacklogItem(Map<String, Object> map){
        this.title = (String) map.get("name");
        this.column = this.convertList((String) map.get("list_name"));
        this.dateCompleted = this.convertDate((String) map.get("due"));
        this.description = (String) map.get("desc");
        this.storyPoints = Integer.parseInt((String) map.get("pts"));
    }

    public int getStoryPoints() {
        return this.storyPoints;
    }

    @Nonnull
    public String getTitle() {
        return this.title;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public Calendar getDateCompleted() {
        return this.dateCompleted;
    }

    @Nonnull
    public BacklogColumn getColumn() {
        return this.column;
    }

    public boolean equals(BacklogItem another){
        if(!this.title.equals(another.getTitle())) return false;
        if(!this.description.equals(another.getDescription())) return false;
        if(this.storyPoints != another.storyPoints) return false;
        return true;
    }

    private Calendar convertDate(String d){

        return null;
    }

    private BacklogColumn convertList(String list){
        String s = list.toLowerCase();
        if(s.contains("done")) return DONE;
        if(s.contains("progress")) return IN_PROGRESS;
        return BACKLOG;
    }
}