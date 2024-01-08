package com.caversia.plugins.votes.commons.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a player event that gets persisted on the database.
 * 
 * @author amartins
 */
@Entity
@Table(name = "players_votes")
//@formatter:off
@NamedQueries({ @NamedQuery(name  = "PlayerVote.getLatestVote", 
                            query = "SELECT v FROM PlayerVote v WHERE v.username=:username ORDER BY v.timestamp DESC"), 
                @NamedQuery(name  = "PlayerVote.getVotesByUsernameAndStatus", 
                            query = "SELECT v FROM PlayerVote v WHERE v.username=:username AND v.status=:status AND v.timestamp>=:startDate"),
                @NamedQuery(name  = "PlayerVote.countPlayerVotes", 
                            query = "SELECT COUNT(v) FROM PlayerVote v WHERE v.username=:username AND v.status=:status AND v.timestamp>=:startDate") 
})
//@formatter:on
@SequenceGenerator(name = "players_votes_seq", sequenceName = "players_votes_seq", initialValue = 1, allocationSize = 10)
public class PlayerVote {

    public enum Status {
        PARSED, QUEUED;
    }

    @Id
    @GeneratedValue(generator = "players_votes_seq")
    private Integer id;

    @Column(name = "username", nullable = false, length = 40)
    private String username;
    
    @Column(name = "player_id",length = 40)
    private String playerId;
    
    @Column(name = "server", nullable = false, columnDefinition = "VARCHAR(20)")
    private String server;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    public PlayerVote() {
        // Empty by design
    }

    /**
     * Constructor.
     * 
     * @param username the user username
     * @param playerId the player UUID
     * @param server the server where the vote was made
     * @param status the vote status
     * @param timestamp when the vote was received
     */
    public PlayerVote(String username, String playerId, String server, Status status, Date timestamp) {
        this.username = username;
        this.playerId = playerId;
        this.server = server;
        this.status = status;
        this.timestamp = timestamp;
    }

    /**
     * Gets the playerId.
     *
     * @return the playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Sets the playerId.
     *
     * @param playerId the playerId to set
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server.
     *
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
