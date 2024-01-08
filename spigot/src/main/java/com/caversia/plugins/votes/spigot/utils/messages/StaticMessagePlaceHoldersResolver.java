package com.caversia.plugins.votes.spigot.utils.messages;

import org.bukkit.entity.Player;

/**
 * Implementation of {@link MessagePlaceHoldersResolver} with static information.
 * 
 * @author amartins
 */
public class StaticMessagePlaceHoldersResolver implements MessagePlaceHoldersResolver {
    private Player player;

    private int numberOfVotes;

    /**
     * Constructor.
     * 
     * @param player the player
     * @param numberOfVotes the number of votes
     */
    public StaticMessagePlaceHoldersResolver(Player player, int numberOfVotes) {
        this.player = player;
        this.numberOfVotes = numberOfVotes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.caversia.plugins.votes.standalone.utils.messages.MessagePlaceHoldersResolver#getPlayer()
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.caversia.plugins.votes.standalone.utils.messages.MessagePlaceHoldersResolver#getNumberOfVotes()
     */
    @Override
    public int getNumberOfVotes() {
        return numberOfVotes;
    }
}
