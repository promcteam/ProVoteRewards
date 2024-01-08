package com.caversia.plugins.votes.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.caversia.plugins.votes.spigot.commands.RootCommands;
import com.caversia.plugins.votes.spigot.listeners.PlayerJoinListener;
import com.caversia.plugins.votes.spigot.listeners.PlayerQuitListener;
import com.caversia.plugins.votes.spigot.listeners.VotesChannelListener;
import com.caversia.plugins.votes.spigot.utils.Logger;
import com.caversia.plugins.votes.spigot.utils.messages.MessageUtils;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.SimpleInjector;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

/**
 * Plugin entry point.
 * 
 * @author sp3c
 * @author marvnfl
 */
public class Main extends JavaPlugin {
    public static Main self;

    private CommandsManager<CommandSender> commandsManager;

    @Override
    public void onEnable() {
        Main.self = this;

        setupCommands();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, MessageUtils.utilsChannelName);
        getServer().getMessenger().registerIncomingPluginChannel(this, VotesChannelListener.votesChannelName,
                new VotesChannelListener());

        Logger.info("Caversia Votes Plugin was enabled.");
    }

    @Override
    public void onDisable() {
        Logger.info("Caversia Votes Plugin was disabled.");
    }

    /**
     * Sets up everything related with the plugin commands.
     */
    private void setupCommands() {
        this.commandsManager = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String permission) {
                return sender.hasPermission(permission);
            }
        };
        commandsManager.setInjector(new SimpleInjector(this));
        CommandsManagerRegistration registration = new CommandsManagerRegistration(this, commandsManager);
        registration.register(RootCommands.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        try {
            this.commandsManager.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }
}
