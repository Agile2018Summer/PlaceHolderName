package edu.harvard.integration;

import edu.harvard.integration.CommandHandler.CommandHandler;
import edu.harvard.integration.Slack.SlackIntegration;
import edu.harvard.integration.Trello.TrelloIntegration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * The main class.
 */
public class Integrator {
    private static Integrator INSTANCE;
    private final Logger logger;
    private final Config config;
    private final CommandHandler cmdHandler;
    private SlackIntegration slack;
    private TrelloIntegration trello;

    private Integrator() {
        INSTANCE = this;
        this.logger = LogManager.getLogger("Trello-Slack-Integrator");
        this.config = new Config();
        this.cmdHandler = new CommandHandler("edu.harvard.integration.CommandHandler.Commands");
        this.slack = new SlackIntegration();
        this.trello = new TrelloIntegration();
    }

    /**
     * Called when {@link Integrator} is stopped to gracefully close all open {@link edu.harvard.integration.api.Integration}s.
     */
    public void stop() {
        if (getSlack() != null)
            getSlack().stop();
        if (getTrello() != null)
            getTrello().stop();
    }

    public static void main(String[] args) {
        new Integrator();
        Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));
    }

    /**
     * Retrieves the {@link Config}.
     * @return The {@link Config}.
     */
    @Nonnull
    public static Config getConfig() {
        return INSTANCE.config;
    }

    /**
     * Retrieves the {@link SlackIntegration}.
     * @return The {@link SlackIntegration}.
     */
    public static SlackIntegration getSlack() {
        return INSTANCE.slack;
    }

    /**
     * Retrieves the {@link TrelloIntegration}.
     * @return The {@link TrelloIntegration}.
     */
    public static TrelloIntegration getTrello() {
        return INSTANCE.trello;
    }

    /**
     * Retrieves the {@link CommandHandler}.
     * @return The {@link CommandHandler}.
     */
    @Nonnull
    public static CommandHandler getCommandHandler() {
        return INSTANCE.cmdHandler;
    }

    /**
     * Retrieves the {@link Logger}.
     * @return The {@link Logger}.
     */
    @Nonnull
    public static Logger getLogger() {
        return INSTANCE.logger;
    }

    /**
     * Retrieves the nonstatic instance of {@link Integrator}.
     * @return The nonstatic instance of {@link Integrator}.
     */
    @Nonnull
    public static Integrator getInstance() {
        return INSTANCE;
    }
}