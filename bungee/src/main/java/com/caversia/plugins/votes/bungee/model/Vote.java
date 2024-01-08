package com.caversia.plugins.votes.bungee.model;

/**
 * Represents a vote received from a website.
 * 
 * @author amartins
 */
public class Vote {
    private String serviceName;

    private String username;

    private String address;

    private String timeStamp;

    /**
     * Constructor.
     * 
     * @param serviceName the name of the service from where the vote was received
     * @param username the username of the player who voted
     * @param address the address of the service
     * @param timeStamp the service timestamp
     */
    public Vote(String serviceName, String username, String address, String timeStamp) {
        this.serviceName = serviceName;
        this.username = username;
        this.address = address;
        this.timeStamp = timeStamp;
    }

    /**
     * Gets the serviceName.
     *
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the serviceName.
     *
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the timeStamp.
     *
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the timeStamp.
     *
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
