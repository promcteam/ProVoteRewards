package com.caversia.plugins.votes.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import com.caversia.plugins.votes.bungee.Main;

/**
 * Wrapper to access and manage this plugin configuration.
 * 
 * @author amartins
 */
public enum Config {
    INSTANCE;

    private Configuration config;

    /**
     * Initializes the configuration. If a configuration file doesn't exists a new one is created.
     * 
     * @throws IOException when creating or loading the configuration file fails
     */
    public void initialize() throws IOException {
        if (!Main.self.getDataFolder().exists()) {
            Main.self.getDataFolder().mkdir();
        }

        File file = new File(Main.self.getDataFolder(), "config.yml");

        if (!file.exists()) {
            Files.copy(Main.self.getResourceAsStream("config.yml"), file.toPath());
        }

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                new File(Main.self.getDataFolder(), "config.yml"));
    }

    /**
     * Reloads the configuration.
     */
    public void reload() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    new File(Main.self.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            Logger.error("Failed to reload the config file: {}", e.getMessage());
        }
    }

    public String getHost() {
        return config.getString("server.host");
    }

    public int getPort() {
        return config.getInt("server.port");
    }
}
