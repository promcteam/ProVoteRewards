package com.caversia.plugins.votes.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;
import com.caversia.plugins.votes.spigot.business.RemindsManager;
import com.caversia.plugins.votes.spigot.model.VoteInfo;
import com.caversia.plugins.votes.spigot.utils.Logger;
import com.caversia.plugins.votes.spigot.utils.RewardsUtils;

/**
 * Listener for the <strong>CaversiaVotesChannel</strong> plugin channel.
 * 
 * @author amartins
 */
public class VotesChannelListener implements PluginMessageListener {

    public static final String votesChannelName = "CaversiaVotesChannel";

    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.plugin.messaging.PluginMessageListener#onPluginMessageReceived(java.lang.String,
     * org.bukkit.entity.Player, byte[])
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(channel)) {
            Logger.warn("Received a message to channel <{}> on a listener to channel <{}>!", channel, votesChannelName);
            return;
        }

        Logger.info("Received vote from: {}", player.getName());
        int nParsedVotes = PersistenceManager.INSTANCE.countCurrentMonthPlayerVotes(player.getName(), Status.PARSED);
        VoteInfo enrichementInfo = new VoteInfo(player, nParsedVotes, false);

        RewardsUtils.INSTANCE.processVote(enrichementInfo);
        RemindsManager.INSTANCE.removeReminderTo(player);
    }
}
