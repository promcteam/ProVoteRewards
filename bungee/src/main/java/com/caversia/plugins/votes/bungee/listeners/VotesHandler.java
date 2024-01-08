package com.caversia.plugins.votes.bungee.listeners;

import java.time.Instant;
import java.util.Date;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.caversia.plugins.votes.bungee.Main;
import com.caversia.plugins.votes.bungee.model.Vote;
import com.caversia.plugins.votes.bungee.model.VoteEvent;
import com.caversia.plugins.votes.bungee.utils.Logger;
import com.caversia.plugins.votes.bungee.utils.Mojang;
import com.caversia.plugins.votes.bungee.utils.Mojang.PlayerInfo;
import com.caversia.plugins.votes.commons.model.PlayerVote;
import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.persistence.PersistenceManager;

/**
 * Implements the logic to be executed when a vote is received.
 * 
 * @author amartins
 */
public class VotesHandler {
    public static final String votesChannelName = "CaversiaVotesChannel";

    /**
     * Handles a received vote by persisting it on the database and broadcasting the event.
     * 
     * @param vote the received vote
     */
    public static void handle(Vote vote) {
        ProxiedPlayer player = Main.self.getProxy().getPlayer(vote.getUsername());
        Status voteStatus = (player == null) ? Status.QUEUED : Status.PARSED;

        PlayerInfo playerInfo = Mojang.getPlayerInfo(vote.getUsername());
        String playerName = playerInfo != null ? playerInfo.getPlayerName() : vote.getUsername();
        String playerId = playerInfo != null ? playerInfo.getPlayerId() : null;

        //@formatter:off
        PlayerVote playerVote = new PlayerVote( playerName,
                                                playerId,
                                                vote.getServiceName(), 
                                                voteStatus, 
                                                Date.from(Instant.now()));
        //@formatter:on

        PersistenceManager.INSTANCE.persist(playerVote);

        if (player == null) {
            Logger.info("Received vote from an offline player with name: {}. The vote was queued...",
                    vote.getUsername());
            return;
        }

        Main.self.broadcastEvent(new VoteEvent(vote));

        player.getServer().sendData(votesChannelName, "voted".getBytes());
    }
}
