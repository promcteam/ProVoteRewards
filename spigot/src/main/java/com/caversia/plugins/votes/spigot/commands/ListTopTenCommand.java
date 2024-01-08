package com.caversia.plugins.votes.spigot.commands;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import com.caversia.plugins.votes.commons.model.TopEntry;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.utils.ChatMessage;
import com.sk89q.minecraft.util.commands.CommandContext;

/**
 * Implements the voting top (/votetop) command.
 * 
 * @author amartins
 */
public class ListTopTenCommand {

    /**
     * Parses and executes the command.
     * 
     * @param player the player that issued the command
     * @param args the command extra arguments
     */
    public void execute(Player player, CommandContext args) {
        List<TopEntry> topTen = PersistenceManager.INSTANCE.getCurrentMonthTopTen();

        //@formatter:off
        ChatMessage.to(player)
                   .yellow("------------------")
                   .white("[ ").gold(ChatColor.BOLD +Month.from(Instant.now().atZone(ZoneId.of("UTC"))).getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                   .darkAqua(" Top Voters ").white("]")
                   .yellow("------------------")
                   .send();
        
        if (topTen.isEmpty()) {
            ChatMessage.to(player)
                       .red("No votes have been received this month. Be the first!")
                       .send();
            return;
        }
        //@formatter:on

        boolean playerListedInTop10 = false;
        for (TopEntry topEntry : topTen) {
            ChatMessage message = ChatMessage.to(player).white(topEntry.getPosition(), 'º');

            String displayName = topEntry.getPlayerName();
            if (topEntry.getPosition() < 10) {
                message.gold("  ");
            } else {
                message.gold(" ");
            }

            ;
            if (topEntry.getPlayerName().equalsIgnoreCase(player.getName())) {
                playerListedInTop10 = true;
                message.darkPurple("<", topEntry.getVotes(), " votes>  ", displayName);
            } else {
                message.gold("<", topEntry.getVotes(), " votes>  ").green(displayName);
            }

            message.send();
        }

        if (playerListedInTop10) {
            return;
        }
        ChatMessage.to(player).red("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").send();

        TopEntry playerRank = PersistenceManager.INSTANCE.getCurrentMonthPlayerRank(player.getName());
        if (playerRank == null) {
            ChatMessage.to(player).red("You haven't voted this month!").send();
        } else {
            ChatMessage message = ChatMessage.to(player).white("#", playerRank.getPosition());
            message.red(player.getName()).white(playerRank.getVotes()).send();
        }
    }
}
