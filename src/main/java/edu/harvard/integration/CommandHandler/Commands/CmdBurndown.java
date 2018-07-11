package edu.harvard.integration.CommandHandler.Commands;

import edu.harvard.integration.CommandHandler.CommandSender;
import edu.harvard.integration.CommandHandler.CommandSource;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.api.Cmd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdBurndown implements Cmd {
    @Override
    public void performCommand(@Nonnull String[] args, @Nonnull CommandSender info) {
        //TODO: This should probably first update the image based on current data
        Integrator.getSlack().sendFile(new File("snapshot.png"));
    }

    @Nonnull
    @Override
    public String helpDoc() {
        return "Displays the most recent burndown chart for the backlog";
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "!burndown";
    }

    @Nonnull
    @Override
    public String getName() {
        return "Burndown";
    }

    @Nullable
    @Override
    public List<String> getAliases() {
        return Arrays.asList("getburndown", "burndownchart", "getburndownchart");
    }

    @Nullable
    @Override
    public List<CommandSource> supportedSources() {
        return Collections.singletonList(CommandSource.Slack);
    }
}