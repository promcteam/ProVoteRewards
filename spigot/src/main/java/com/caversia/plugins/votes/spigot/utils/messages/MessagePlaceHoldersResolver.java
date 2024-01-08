package com.caversia.plugins.votes.spigot.utils.messages;

import org.bukkit.entity.Player;

/**
 * Interface that defines a contract to obtain info when filling placeholders on configured messages.
 * 
 * @author amartins
 */
public interface MessagePlaceHoldersResolver {
    /**
     * Gets the {@link Player} being processed.
     * 
     * @return the {@link Player} being processed.
     */
    Player getPlayer();

    /**
     * Gets the number of votes that the current {@link Player} has made this month.
     * 
     * @return the number of votes that the current {@link Player} has made this month.
     */
    int getNumberOfVotes();
}
