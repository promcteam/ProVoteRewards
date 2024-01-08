package com.caversia.plugins.votes.bungee.model;

import net.md_5.bungee.api.plugin.Event;

/**
 * Represents a received vote event.
 * 
 * @author amartins
 */
public class VoteEvent extends Event {
    private Vote vote;

    /**
     * Constructor.
     * 
     * @param vote the received vote
     */
    public VoteEvent(final Vote vote) {
        this.vote = vote;
    }

    /**
     * Gets the vote.
     * 
     * @return the vote.
     */
    public Vote getVote() {
        return this.vote;
    }
}
