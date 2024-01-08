package com.caversia.plugins.votes.spigot.business;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.Main;
import com.caversia.plugins.votes.spigot.utils.Config;
import com.caversia.plugins.votes.spigot.utils.messages.MessagePlaceHoldersResolver;
import com.caversia.plugins.votes.spigot.utils.messages.MessageUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Manages the voting remind messages that are sent to players who haven't voted for more than 24 hours.
 * 
 * @author amartins
 */
public enum RemindsManager {
    INSTANCE;

    private static final int ticksPerSecond = 20;
    private Cache<UUID, BukkitTask> cache = CacheBuilder.newBuilder().build();

    /**
     * Adds a new reminder to a given player.
     * 
     * @param player the player to add the reminder
     */
    public void addReminderTo(Player player) {
        int interval = Config.INSTANCE.getRemindsInterval() * ticksPerSecond;

        // Remove any pending reminder for this player
        removeReminderTo(player);

        //@formatter:off
        BukkitTask task = Bukkit.getScheduler()
                                .runTaskTimer(Main.self, () -> remindTick(player.getUniqueId()), 0, interval);
        //@formatter:on

        cache.put(player.getUniqueId(), task);
    }

    /**
     * Removes a reminder from a player.
     * 
     * @param player the player to have the reminder removed
     */
    public void removeReminderTo(Player player) {
        BukkitTask task = cache.getIfPresent(player.getUniqueId());
        if (task != null) {
            task.cancel();
            cache.invalidate(task);
        }
    }

    /**
     * Handles the tick of a player reminder.
     * 
     * @param playerId the id of the player who is to be reminded
     */
    private void remindTick(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);

        if (player == null || !player.isOnline()) {
            BukkitTask task = cache.getIfPresent(playerId);
            if (task != null) {
                task.cancel();
                cache.invalidate(task);
            }
            return;
        }

        // Lazy evaluate the number of votes to avoid unnecessary queries to the db
        MessageUtils.dispatch(Config.INSTANCE.getRemindsMessages(), new MessagePlaceHoldersResolver() {
            @Override
            public Player getPlayer() {
                return player;
            }

            @Override
            public int getNumberOfVotes() {
                return PersistenceManager.INSTANCE.countCurrentMonthPlayerVotes(player.getName(), Status.PARSED);
            }
        });
    }
}