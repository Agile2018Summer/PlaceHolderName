package edu.harvard.integration.Trello;

import edu.harvard.integration.Config;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.api.Integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * An implementation of {@link edu.harvard.integration.api.Integration} to connect to the Trello server.
 */
public class TrelloIntegration implements Integration {
    private final String token;
    private final String key;
    private final String interval;
    private boolean isConnected = false;
    private TrelloConnection conn = null;
    static List<Map<String, String>> trelloInfo = null;

    public TrelloIntegration() {
        Config config = Integrator.getConfig();
        this.token = config.getOrDefault("TRELLO_TOKEN", "trello_token");
        this.key = config.getOrDefault("TRELLO_KEY", "trello_key");
        this.interval = config.getOrDefault("TRELLO_INTERVAL", "300");
        if (this.token.equals("trello_token") || this.key.equals("trello_key")) {
            Integrator.getLogger().error("Failed to load needed configs for Trello Integration");
            return;
        }
        if (connect()){
            Integrator.getLogger().info("Connected to Trello.");
            //this.StartListen(Integer.parseInt(this.interval));
        }
        else
            Integrator.getLogger().error("Failed to connect to Trello.");
    }

    public TrelloIntegration(String key, String token){
        this.token = token;
        this.key = key;
        this.interval = "300";
        if (connect()){
            System.out.println("Connected to Trello.");
            //this.StartListen(Integer.parseInt(this.interval));
        }
        else
            System.out.println("Failed to connect to Trello.");
    }

    /**
     *
     * Connects to the Trello server.
     * @return True if this {@link edu.harvard.integration.api.Integration} connected successfully, false otherwise.
     */
    private boolean connect() {
        this.conn = new TrelloConnection(this.token, this.key);
        List b = this.conn.trackAllBoards();
        if(b == null) return false;
        return true;
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

    /**
     * Get trello information by board name and list (in progress, done, etc).
     */
    public List<Map<String, Object>> getInfoByList(String boardName, String listName){
        List<Map<String, String>> boards = this.getAllTrelloBoards();
        for(Map<String, String> board : boards){
            if(board.get("name").equals(boardName)){
                String boardId = board.get("id");
                List<Map<String, Object>> lists = BoardUtils.getBoardLists(boardId);
                if(lists == null || lists.size() == 0){
                    Integrator.getLogger().error("Cannot find a list with given name.");
                    return null;
                }
                for(Map<String, Object> list : lists){
                    if(list.get("name").equals(listName)){
                        return ListUtils.getListContent((String) list.get("id"));
                    }
                }
                Integrator.getLogger().error("Cannot find a list with given name.");
                return null;
            }
        }
        Integrator.getLogger().error("Cannot find a board with given name.");
        return null;
    }

    /**
     * Set listener to get Trello information by a given time (in seconds)
     */
    public void StartListen(int interval){
        ExecutorService executor = Executors.newCachedThreadPool();
        class ParseTask implements Callable<String> {
            public String call() throws Exception {
                while(true){
                    List<Map<String, String>> oldInfo = new ArrayList<>(trelloInfo);
                    trelloInfo = getAllTrelloBoards();
                    Thread.sleep(interval * 1000);
                }
            }
        }
        ParseTask task = new ParseTask();
        FutureTask<String> futureTask = new FutureTask<String>(task);
        executor.submit(futureTask);
    }
}