package com.caversia.plugins.votes.spigot.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.caversia.plugins.votes.spigot.Main;
import com.caversia.plugins.votes.spigot.utils.Config;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;

/**
 * @author amartins
 */
public class VotesOPCommands {
    /**
     * Default constructor.
     * 
     * @param instance this plugin instance
     */
    public VotesOPCommands(Main instance) {
    }

    //@formatter:off
    @Command(aliases = {"reload", "rl"}, 
             desc = "Reloads the plugin configuration.", 
             help = "Reloads the plugin configuration.")
    //@formatter:on
    public void reload(CommandContext args, CommandSender sender) {
        if (sender.isOp()) {
            Config.INSTANCE.reload();
            sender.sendMessage(ChatColor.GREEN + "The configuration was reloaded successfully!");
        }
    }

    //@formatter:off
    @Command(aliases = {"list", "ls"}, 
             max = 1,
             desc = "Reloads the plugin configuration.", 
             help = "Reloads the plugin configuration.")
    //@formatter:on
    public void list(CommandContext args, CommandSender sender) {
        if (sender.isOp()) {
            new ListVotersCommand().execute((Player) sender, args);
        }
    }
}