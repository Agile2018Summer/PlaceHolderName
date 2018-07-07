package edu.harvard.integration.Trello;

import edu.harvard.integration.Config;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.api.Integration;

import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link edu.harvard.integration.api.Integration} to connect to the Trello server.
 */
public class TrelloIntegration implements Integration {
    private final String token;
    private final String key;
    private boolean isConnected = false;
    private TrelloConnection conn = null;

    public TrelloIntegration() {
        Config config = Integrator.getConfig();
        this.token = config.getOrDefault("TRELLO_TOKEN", "trello_token");
        this.key = config.getOrDefault("TRELLO_KEY", "trello_key");
        if (this.token.equals("trello_token") || this.key.equals("trello_key")) {
            Integrator.getLogger().error("Failed to load needed configs for Trello Integration");
            return;
        }
        if (connect())
            Integrator.getLogger().info("Connected to Trello.");
        else
            Integrator.getLogger().error("Failed to connect to Trello.");
    }

    /**
     *
     * Connects to the Trello server.
     * @return True if this {@link edu.harvard.integration.api.Integration} connected successfully, false otherwise.
     */
    private boolean connect() {
        this.conn = new TrelloConnection(this.token, this.key);
        return false;
    }

    @Override
    public void stop() {

    }

    /**
     * Get names of all Trello boards bound to current user.
    * */
    public List<Map<String, String>> getAllTrelloBoards(){
        return this.conn.trackAllBoards();
    }
}