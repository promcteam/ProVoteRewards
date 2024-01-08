package com.caversia.plugins.votes.bungee;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;

import com.caversia.plugins.votes.bungee.business.VotesReceiver;
import com.caversia.plugins.votes.bungee.listeners.VotesHandler;
import com.caversia.plugins.votes.bungee.utils.Config;
import com.caversia.plugins.votes.bungee.utils.Logger;

/**
 * Plugin entry point.
 * 
 * @author sp3c
 * @author marvnfl
 */
public class Main extends Plugin {
    public static Main self;

    private VotesReceiver voteReceiver;

    @Override
    public void onEnable() {
        Main.self = this;

        try {
            Config.INSTANCE.initialize();

            voteReceiver = new VotesReceiver();
            voteReceiver.start(Config.INSTANCE.getHost(), Config.INSTANCE.getPort());
        } catch (Exception e) {
            Logger.error("Failed to start: {}", e);
            return;
        }

        getProxy().registerChannel(VotesHandler.votesChannelName);
        Logger.info("Caversia Bungee Votes Plugin was enabled.");
    }

    @Override
    public void onDisable() {
        Logger.info("Caversia Bungee Votes Plugin was disabled.");
    }

    /**
     * Runs a task asynchronously.
     * 
     * @param task the task to run
     */
    public void runAsyncTask(Runnable task) {
        getProxy().getScheduler().runAsync(this, task);
    }

    /**
     * Broadcasts a plugin {@link Event}.
     * 
     * @param event the event to broadcast
     */
    public void broadcastEvent(Event event) {
        runAsyncTask(() -> getProxy().getPluginManager().callEvent(event));
    }
}
