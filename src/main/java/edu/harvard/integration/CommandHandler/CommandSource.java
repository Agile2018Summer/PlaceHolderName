package edu.harvard.integration.CommandHandler;

/**
 * An enum for which platform {@link edu.harvard.integration.api.Cmd}s can come from.
 */
public enum CommandSource {
    /**
     * The {@link CommandSource} of {@link edu.harvard.integration.api.Cmd} is Slack
     */
    Slack("Slack"),
    /**
     * The {@link CommandSource} of {@link edu.harvard.integration.api.Cmd} is the Console
     */
    Console("Console");//TODO: Implement some commands for the console in the future

    private final String name;

    CommandSource(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this {@link CommandSource}.
     * @return The name of this {@link CommandSource}.
     */
    public String getName() {
        return this.name;
    }
}