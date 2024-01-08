package com.caversia.plugins.votes.bungee.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Provides utility functions related with the Mojang minecraft HTTP services.
 * 
 * @author amartins
 */
public class Mojang {
    private static final String mojangUserInfoURL = "https://api.mojang.com/profiles/minecraft";

    /**
     * Gets the player info from the Mojang services.
     * 
     * @param name the player name
     * @return the obtained {@link PlayerInfo}.
     */
    public static PlayerInfo getPlayerInfo(String name) {
        try {
            String body = "[\"" + name + "\"]";
            JsonElement jsonElement = new JsonParser().parse(HttpUtils.executePOST(mojangUserInfoURL, body));
            String playerName = jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String playerId = jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
            playerId = playerId.replaceFirst(
                    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");

            return new PlayerInfo(playerName, playerId);
        } catch (Exception e) {
            Logger.warn("Failed to fetch the player info from Mojang: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Represents information obtained from Mojang about a player.
     * 
     * @author amartins
     */
    public static class PlayerInfo {
        private String playerName;
        private String playerId;

        /**
         * Constructor.
         * 
         * @param playerName the player name on Mojang
         * @param playerId the player UUID
         */
        public PlayerInfo(String playerName, String playerId) {
            this.playerName = playerName;
            this.playerId = playerId;
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
    }
}
