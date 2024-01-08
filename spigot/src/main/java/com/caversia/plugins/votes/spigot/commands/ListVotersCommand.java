package com.caversia.plugins.votes.spigot.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.caversia.plugins.votes.commons.model.TopEntry;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.utils.ChatMessage;
import com.sk89q.minecraft.util.commands.CommandContext;

/**
 * Handles the <strong>/cvr list</strong> command.
 * 
 * @author amartins
 */
public class ListVotersCommand {
    private static final int ITEMS_PER_PAGE = 15;

    /**
     * Parses and executes the command.
     * 
     * @param player the player that issued the command
     * @param args the command extra arguments
     */
    public void execute(Player player, CommandContext args) {
        int pageNumber = 1;
        if (args.argsLength() != 0) {
            pageNumber = args.getInteger(0);
        }

        List<TopEntry> top = PersistenceManager.INSTANCE.getCurrentMonthTop(pageNumber);
        int pagesAvailable = (PersistenceManager.INSTANCE.countThisMonthDistinctVoters() / ITEMS_PER_PAGE) + 1;
        if (pageNumber < 1 || pageNumber > pagesAvailable) {
            ChatMessage.to(player).red("Invalid page number. Select a page between 1 and ", pagesAvailable).send();
            return;
        }

        printPageHeader(player, pageNumber, pagesAvailable);
        top.forEach(e -> ChatMessage.to(player)
                .white(e.getPosition(), "º", "  <", e.getVotes(), " votes>", "  ", e.getPlayerName()).send());
    }

    /**
     * Prints an items listing page header on a player chat.
     * 
     * @param player the player where the header is to be printed
     * @param pageNumber the number of the page being printed
     * @param pagesAvailable the number of pages available
     */
    private void printPageHeader(Player player, int pageNumber, int pagesAvailable) {
        //@formatter:off
        ChatMessage.to(player)
                   .bold()
                   .yellow("-------- Listing ").lPurple("Voters")
                   .yellow(" page ").lPurple(pageNumber).yellow(" of ").lPurple(pagesAvailable)
                   .yellow(" --------")
                   .send();
        //@formatter:on
    }
}
