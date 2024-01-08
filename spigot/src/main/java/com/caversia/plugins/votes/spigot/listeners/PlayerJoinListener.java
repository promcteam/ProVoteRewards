package com.caversia.plugins.votes.spigot.listeners;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.caversia.plugins.votes.commons.model.PlayerVote;
import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.business.RemindsManager;
import com.caversia.plugins.votes.spigot.model.VoteInfo;
import com.caversia.plugins.votes.spigot.utils.Config;
import com.caversia.plugins.votes.spigot.utils.RewardsUtils;
import com.caversia.plugins.votes.spigot.utils.messages.MessageUtils;
import com.caversia.plugins.votes.spigot.utils.messages.StaticMessagePlaceHoldersResolver;

/**
 * Listens for player login events and parse any pending rewards.
 * 
 * @author amartins
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        int nVotes = parseQueuedVotes(player);

        StaticMessagePlaceHoldersResolver resolver = new StaticMessagePlaceHoldersResolver(player, nVotes);
        MessageUtils.dispatch(Config.INSTANCE.getJoinMessages(), resolver);

        PlayerVote latestVote = PersistenceManager.INSTANCE.getLatestVote(player.getName());
        if (latestVote == null || ChronoUnit.HOURS.between(latestVote.getTimestamp().toInstant(), Instant.now()) > 24) {
            RemindsManager.INSTANCE.addReminderTo(player);
        }
    }

    /**
     * Parses any queued votes that the player may have and give him the rewards.
     * 
     * @param player the player to check for queued votes
     * @return the current number of player votes
     */
    private int parseQueuedVotes(Player player) {
        int nVotes = PersistenceManager.INSTANCE.countCurrentMonthPlayerVotes(player.getName(), Status.PARSED);

        //@formatter:off
        List<PlayerVote> queuedVotes = 
                PersistenceManager.INSTANCE.getCurrentMonthVotesByPlayerNameAndStatus(player.getName(), Status.QUEUED);
        //@formatter:on

        if (queuedVotes.isEmpty()) {
            return nVotes;
        }

        for (PlayerVote playerVote : queuedVotes) {
            RewardsUtils.INSTANCE.processVote(new VoteInfo(player, nVotes, true));
            playerVote.setStatus(Status.PARSED);
            PersistenceManager.INSTANCE.save(playerVote);
            nVotes++;
        }

        return nVotes;
    }
}
