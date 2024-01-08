package com.caversia.plugins.votes.commons.model;

/**
 * @author amartins
 */
public class TopEntry {
    private int position;

    private String playerName;

    private int votes;

    /**
     * @param position
     * @param playerName
     * @param votes
     */
    public TopEntry(int position, String playerName, int votes) {
        this.position = position;
        this.playerName = playerName;
        this.votes = votes;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the playerName.
     *
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the playerName.
     *
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the votes.
     *
     * @return the votes
     */
    public int getVotes() {
        return votes;
    }

    /**
     * Sets the votes.
     *
     * @param votes the votes to set
     */
    public void setVotes(int votes) {
        this.votes = votes;
    }
}
