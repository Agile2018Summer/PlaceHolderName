package edu.harvard.integration.Slack;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import javax.annotation.Nonnull;

public final class SlackUser {
    private final String id;
    private final String name, displayName;
    private final String rank;

    SlackUser(JsonObject json) {
        this.id = json.getString(Jsoner.mintJsonKey("id", null));
        this.name = json.getString(Jsoner.mintJsonKey("name", null));
        this.displayName = json.getStringOrDefault(Jsoner.mintJsonKey("real_name", this.name));
        if (json.getBooleanOrDefault(Jsoner.mintJsonKey("is_bot", false)))
            this.rank = "BOT";
        else if (json.getBooleanOrDefault(Jsoner.mintJsonKey("is_primary_owner", false)) || json.getBooleanOrDefault(Jsoner.mintJsonKey("is_owner", false)))
            this.rank = "OWNER";
        else if (json.getBooleanOrDefault(Jsoner.mintJsonKey("is_admin", false)))
            this.rank = "ADMIN";
        else if (json.getBooleanOrDefault(Jsoner.mintJsonKey("is_ultra_restricted", false)) || json.getBooleanOrDefault(Jsoner.mintJsonKey("is_restricted", false)))
            this.rank = "BANNED";
        else
            this.rank = "MEMBER";
    }

    /**
     * Retrieves the name of this {@link SlackUser}.
     * @return The name of this {@link SlackUser}.
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the display name of this {@link SlackUser}.
     * @return The display name of this {@link SlackUser}.
     */
    @Nonnull
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Retrieves the Slack ID of this {@link SlackUser}.
     * @return The Slack ID of this {@link SlackUser}.
     */
    @Nonnull
    public String getID() {
        return this.id;
    }

    /**
     * Retrieves the rank of this {@link SlackUser}.
     * @return The rank of this {@link SlackUser}.
     */
    @Nonnull
    public String getRank() {
        return this.rank;
    }

    /**
     * Checks if this {@link SlackUser} is a bot.
     * @return True if this user is a bot, false otherwise.
     */
    public boolean isBot() {
        return this.rank.equals("BOT");
    }
}