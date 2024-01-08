package com.caversia.plugins.votes.spigot.utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.caversia.plugins.votes.spigot.Main;

/**
 * Wrapper to access and manage this plugin configuration.
 * 
 * @author amartins
 */
public enum Config {
    INSTANCE;

    private FileConfiguration config;

    /**
     * Initializes the configuration. If a configuration file doesn't exists a new one is created.
     * 
     * @throws IOException when creating or loading the configuration file fails
     */
    private Config() {
        Main.self.saveDefaultConfig();
        config = Main.self.getConfig();
    }

    /**
     * Reloads the configuration.
     */
    public void reload() {
        Main.self.reloadConfig();
        config = Main.self.getConfig();
    }

    /**
     * Gets the messages to broadcast when a player joins the server.
     * 
     * @return the messages to broadcast when a player joins the server.
     */
    public Map<String, List<String>> getJoinMessages() {
        return getMessagesFromSection("general.join");
    }

    /**
     * Gets the messages to whisper when a player executes the <strong>/vote</strong> command.
     * 
     * @return the messages to whisper when a player executes the <strong>/vote</strong> command.
     */
    public Map<String, List<String>> getVoteMessages() {
        return getMessagesFromSection("general.vote");
    }

    //
    // Reminds
    //

    /**
     * Gets the vote reminding interval in seconds.
     * 
     * @return the vote reminding interval in seconds.
     */
    public int getRemindsInterval() {
        return config.getInt("general.reminds.interval");
    }

    /**
     * Gets the vote reminding messages.
     * 
     * @return the vote reminding messages.
     */
    public Map<String, List<String>> getRemindsMessages() {
        return getMessagesFromSection("general.reminds");
    }

    //
    // Default rewards
    //

    /**
     * Gets the messages to be presented when a default reward is given to a player.
     * 
     * @return the messages to be presented when a default reward is given to a player.
     */
    public Map<String, List<String>> getDefaultRewardsMessages() {
        return getMessagesFromSection("rewards.default");
    }

    /**
     * Gets the commands to be executed when a default reward is given to a player.
     * 
     * @return the commands to be executed when a default reward is given to a player.
     */
    public List<String> getDefaultRewardsCommands() {
        return config.getStringList("rewards.default.commands");
    }

    //
    // Lucky rewards
    //

    /**
     * Gets the lucky rewards tiers. A tier <strong>50</strong> means 1 chance in 50 to get a reward.
     * 
     * @return the lucky reward tiers.
     */
    public List<Integer> getLuckyRewardsTiers() {
        //@formatter:off
        return config.getConfigurationSection("rewards.lucky")
                     .getKeys(false)
                     .stream()
                     .map(Integer::valueOf)
                     .collect(toList());
        //@formatter:on
    }

    /**
     * Gets the messages to be presented when a lucky reward is given to a player.
     * 
     * @return the messages to be presented when a lucky reward is given to a player.
     */
    public Map<String, List<String>> getLuckyRewardsMessages(Integer tier) {
        return getMessagesFromSection("rewards.lucky." + tier.toString());
    }

    /**
     * Gets the commands to be executed when a lucky reward is given to a player.
     * 
     * @return the commands to be executed when a lucky reward is given to a player.
     */
    public List<String> getLuckyRewardsCommands(Integer tier) {
        return config.getStringList("rewards.lucky." + tier.toString() + ".commands");
    }

    //
    // Cumulative rewards
    //

    /**
     * Gets the cumulative rewards tiers. A tier <strong>50</strong> means that a player will receive the rewards on
     * that tier after voting 50 times.
     * 
     * @return the cumulative reward tiers.
     */
    public List<Integer> getCumulativeRewardsTiers() {
        return config.getConfigurationSection("rewards.cumulative").getKeys(false).stream().map(Integer::valueOf)
                .collect(toList());
    }

    /**
     * Gets the messages to be presented when a cumulative reward is given to a player.
     * 
     * @return the messages to be presented when a cumulative reward is given to a player.
     */
    public Map<String, List<String>> getCumulativeRewardsMessages(Integer tier) {
        return getMessagesFromSection("rewards.cumulative." + tier.toString());
    }

    /**
     * Gets the commands to be executed when a cumulative reward is given to a player.
     * 
     * @return the commands to be executed when a cumulative reward is given to a player.
     */
    public List<String> getCumulativeRewardsCommands(Integer tier) {
        return config.getStringList("rewards.cumulative." + tier.toString() + ".commands");
    }

    //
    // Permission rewards
    //

    /**
     * Gets the permissions rewards tiers. Those tiers are identified by permissions, when a player has one of those
     * permissions he MUST get those rewards instead of the default ones.
     * 
     * @return the permissions reward tiers.
     */
    public Set<String> getPermissionsRewardsTiers() {
        //@formatter:off
        return config.getConfigurationSection("rewards.permissions")
                     .getKeys(false)
                     .stream()
                     .map(k -> k.replaceAll("_", "."))
                     .peek(p -> Logger.info("Config has rewards for permission: {}", p))
                     .collect(toSet());
        //@formatter:on
    }

    /**
     * Gets the messages to be presented when a permission reward is given to a player.
     * 
     * @return the messages to be presented when a permission reward is given to a player.
     */
    public Map<String, List<String>> getPermissionsRewardsMessages(String tier) {
        return getMessagesFromSection("rewards.permissions." + tier.replace('.', '_'));
    }

    /**
     * Gets the commands to be executed when a permission reward is given to a player.
     * 
     * @return the commands to be executed when a permission reward is given to a player.
     */
    public List<String> getPermissionsRewardsCommands(String tier) {
        return config.getStringList("rewards.permissions." + tier.replace('.', '_') + ".commands");
    }

    /**
     * Gets the chat messages defined on a configuration section.
     * 
     * @param sectionPath the section base path of where the messages are defined
     * @return the chat messages defined on a configuration section.
     */
    private Map<String, List<String>> getMessagesFromSection(String sectionPath) {
        Map<String, List<String>> messages = new LinkedHashMap<>();

        sectionPath += ".messages";
        messages.put("private", config.getStringList(sectionPath + ".private"));
        messages.put("global", config.getStringList(sectionPath + ".broadcast.global"));
        messages.put("server", config.getStringList(sectionPath + ".broadcast.server"));

        return messages;
    }
}