package com.caversia.plugins.votes.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.Main;
import com.caversia.plugins.votes.spigot.utils.Config;
import com.caversia.plugins.votes.spigot.utils.messages.MessagePlaceHoldersResolver;
import com.caversia.plugins.votes.spigot.utils.messages.MessageUtils;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;

/**
 * Lists all the root commands that this plugin supports.
 * 
 * @author sp3c
 */
public class RootCommands {
    /**
     * Default constructor.
     * 
     * @param instance this plugin instance
     */
    public RootCommands(Main instance) {
        // Empty by design
    }

    @Command(aliases = { "votetop" }, desc = "Displays the top 10 voters")
    public void voteTop(CommandContext args, CommandSender sender) {
        new ListTopTenCommand().execute((Player) sender, args);
    }

    @Command(aliases = { "vote" }, desc = "Displays the vote info")
    public void vote(CommandContext args, CommandSender sender) {
        MessageUtils.dispatch(Config.INSTANCE.getVoteMessages(), new MessagePlaceHoldersResolver() {

            @Override
            public Player getPlayer() {
                return (Player) sender;
            }

            @Override
            public int getNumberOfVotes() {
                return PersistenceManager.INSTANCE.countCurrentMonthPlayerVotes(sender.getName(), Status.PARSED);
            }
        });
    }

    @Command(aliases = { "cvr" }, desc = "Root command to this plugin op operations")
    @NestedCommand(VotesOPCommands.class)
    public void cvr(CommandContext args, CommandSender sender) {
        // Empty by design
    }
}
