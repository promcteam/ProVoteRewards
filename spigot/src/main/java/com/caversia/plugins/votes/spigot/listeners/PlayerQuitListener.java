package com.caversia.plugins.votes.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.caversia.plugins.votes.spigot.business.RemindsManager;

/**
 * Listens for player quit events.
 * 
 * @author amartins
 */
public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        Player player = evt.getPlayer();
        RemindsManager.INSTANCE.removeReminderTo(player);
    }
}
