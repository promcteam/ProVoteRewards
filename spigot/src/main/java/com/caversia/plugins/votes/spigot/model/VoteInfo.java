package com.caversia.plugins.votes.spigot.model;

import org.bukkit.entity.Player;

/**
 * Represents a player vote.
 * 
 * @author amartins
 */
public class VoteInfo {
    private Player player;

    private int numberOfVotes;

    private boolean queuedVote;

    /**
     * Constructor.
     * 
     * @param player the player
     * @param numberOfVotes the total number of votes that the player has already made
     * @param queuedVote if this vote has been queued
     */
    public VoteInfo(Player player, int numberOfVotes, boolean queuedVote) {
        this.player = player;
        this.numberOfVotes = numberOfVotes;
        this.queuedVote = queuedVote;
    }

    /**
     * Gets the queuedVote.
     *
     * @return the queuedVote
     */
    public boolean isQueuedVote() {
        return queuedVote;
    }

    /**
     * Sets the queuedVote.
     *
     * @param queuedVote the queuedVote to set
     */
    public void setQueuedVote(boolean queuedVote) {
        this.queuedVote = queuedVote;
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the numberOfVotes.
     *
     * @return the numberOfVotes
     */
    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    /**
     * Sets the numberOfVotes.
     *
     * @param numberOfVotes the numberOfVotes to set
     */
    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }
}
