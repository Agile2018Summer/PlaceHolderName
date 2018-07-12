package edu.harvard.integration.CommandHandler.Commands;

import edu.harvard.integration.CommandHandler.CommandSender;
import edu.harvard.integration.Trello.BacklogItem;
import edu.harvard.integration.Trello.BoardUtils;
import edu.harvard.integration.Trello.Commons;
import edu.harvard.integration.Trello.ListUtils;
import edu.harvard.integration.api.Cmd;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class CmdRetriveByList implements Cmd {
    @Override
    public void performCommand(@Nonnull String[] args, @Nonnull CommandSender sender) {
        String concatedArg = Commons.concatArr(args);
        String[] actualArgs = concatedArg.split("@@");
        if(actualArgs.length != 3){
            sender.sendMessage("Format error, should be: !getbylist @@<board_name> @@<list_name>");
            return;
        }
        String boardName = actualArgs[1].trim();
        String listName = actualArgs[2].trim();
        try {
            String boardId =
                    BoardUtils.getBoardIdByName(Commons.getTrelloToken(), Commons.getTrelloKey(), boardName);
            String listId = ListUtils.getListIdByName(boardId, listName);
            List<Map<String, Object>> cardsInfo = ListUtils.getListContent(listId);
            StringBuilder output = new StringBuilder();
            List<BacklogItem> pbis = Commons.getCardsDetails(cardsInfo);
            for (BacklogItem pbi : pbis) {
                output.append(pbi.toSimpleString());
            }
            sender.sendMessage(output.toString());
        }catch (Exception e){
            sender.sendMessage("Unfortunately you don't have access to a board or list with that name.");
        }
    }

    @Nonnull
    @Override
    public String helpDoc() {
        return "Return all cards in the given board belongs to a specific list.";
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "!getList @@<board_name> @@<list_name>";
    }

    @Nonnull
    @Override
    public String getName() {
        return "getByList";
    }
}
