package edu.harvard.integration.CommandHandler.Commands;

import edu.harvard.integration.ChartHelper;
import edu.harvard.integration.CommandHandler.CommandSender;
import edu.harvard.integration.CommandHandler.CommandSource;
import edu.harvard.integration.Integrator;
import edu.harvard.integration.Trello.BacklogItem;
import edu.harvard.integration.Trello.Commons;
import edu.harvard.integration.Trello.TrelloIntegration;
import edu.harvard.integration.Utils;
import edu.harvard.integration.api.Cmd;
import javafx.application.Application;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CmdBurndown implements Cmd {
    @Override
    public void performCommand(@Nonnull String[] args, @Nonnull CommandSender info) {
        if (args.length == 0) {
            info.sendMessage("Error: You must ender a board name with the format \"@@<board_name>\".");
            return;
        }

        info.sendMessage("Finding board with corresponding name.");
        String boardName = Commons.concatArr(args).replace("@@", "");
        Set<BacklogItem> items;
        try {
            List<BacklogItem> itemsList = TrelloIntegration.getAllPBIs(boardName, Commons.getTrelloToken(), Commons.getTrelloKey());
            items = new HashSet<>(itemsList);
        } catch (Exception e) {
            info.sendMessage("Error: Unfortunately you don't have access to a board with that name.");
            return;
        }

        ArrayList<String> dateStrings = Integrator.getConfig().getOrDefault("DATES", new ArrayList<>());

        ChartHelper.setData(items, dateStrings.stream().map(Utils::getDate).collect(Collectors.toList()), true);
        info.sendMessage("Generating Burndown Chart for board \"" + boardName + "\", please wait...");
        Application.launch(ChartHelper.class);
        File file = new File("snapshot.png");

        if (!file.exists()) {
            info.sendMessage("Error: Something went wrong generating the burndown chart.");
            return;
        }

        info.sendMessage("Uploading Burdown Chart for board \"" + boardName + "\" to Slack, please wait...");
        Integrator.getSlack().sendFile(file);
    }

    @Nonnull
    @Override
    public String helpDoc() {
        return "Displays the most recent Burndown Chart for the given backlog.";
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "!burndown @@<board_name>";
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