package edu.harvard.integration.Trello;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;

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
}