package com.caversia.plugins.votes.spigot.utils.messages;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;

import com.caversia.plugins.votes.spigot.Main;
import com.caversia.plugins.votes.spigot.utils.Logger;
import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Provides utility methods to handle with parsing and dispatching messages obtained from configuration files.
 * 
 * @author amartins
 */
public class MessageUtils {
    public static final String utilsChannelName = "CaversiaUtilsChannel";

    /**
     * Dispatches configured chat messages.
     * <ul>
     * <li><strong>private</strong> messages are whispered to the player only</li>
     * <li><strong>server</strong> messages are broadcasted to the current server chat</li>
     * <li><strong>global</strong> messages are broadcasted to all the servers</li>
     * </ul>
     * 
     * @param messages the messages to be dispatched
     * @param info the info to use on the messages placeholders enrichment and used to obtain the player being messaged
     */
    public static void dispatch(Map<String, List<String>> messages, MessagePlaceHoldersResolver info) {
        Map<String, List<String>> parsedMessages = parseMessages(messages, info);

        for (Entry<String, List<String>> message : parsedMessages.entrySet()) {
            switch (message.getKey()) {
            case "private":
                message.getValue().forEach(
                        m -> info.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', m)));
                break;

            case "server":
                message.getValue().forEach(
                        m -> Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', m)));
                break;

            case "global":
                //@formatter:off
                List<String> lines = message.getValue()
                                            .stream()
                                            .map(m -> ChatColor.translateAlternateColorCodes('&', m))
                                            .collect(toList());
                //@formatter:on

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("broadcastMessage");
                out.writeUTF("ALL");
                out.writeUTF(Joiner.on('\n').join(lines));
                info.getPlayer().sendPluginMessage(Main.self, utilsChannelName, out.toByteArray());
                break;

            default:
                Logger.warn("Unable to dispatch unknown message type: {}", message.getKey());
            }
        }
    }

    /**
     * Parses chat messages by removing empty entries and enriching them with the provided info.
     * 
     * @param messages the messages to parse
     * @param info the info to use on the messages placeholders enrichment
     * @return the parsed messages.
     */
    private static Map<String, List<String>> parseMessages(Map<String, List<String>> messages,
            MessagePlaceHoldersResolver info) {
        Map<String, List<String>> results = new LinkedHashMap<>();
        for (Entry<String, List<String>> messageEntry : messages.entrySet()) {
            if (!messageEntry.getValue().isEmpty()) {
                results.put(messageEntry.getKey(), enrichMessages(messageEntry.getValue(), info));
            }
        }
        return results;
    }

    /**
     * Enrich a list of chat messages by filling their placeholders with info provided on a
     * {@link MessagePlaceHoldersResolver}.
     * 
     * @param messages the messages to enrich
     * @param info the info to use on the placeholders
     * @return the messages with the placeholders replaced with the provided info.
     */
    private static List<String> enrichMessages(List<String> messages, MessagePlaceHoldersResolver info) {
        return messages.stream().map(m -> enrichString(m, info)).collect(toList());
    }

    /**
     * Enriches a string by filling its placeholders with info provided on a {@link MessagePlaceHoldersResolver}.
     * 
     * @param message the message to enrich
     * @param info the info to use on the placeholders
     * @return the message with the placeholders replaced with the provided info.
     */
    private static String enrichString(String message, MessagePlaceHoldersResolver info) {
        if (message.contains("%PlayerName%")) {
            message = message.replaceAll("%PlayerName%", info.getPlayer().getName());
        }

        if (message.contains("%NumberOfVotes%")) {
            String votes = String.valueOf(info.getNumberOfVotes());
            message = message.replaceAll("%NumberOfVotes%", votes);
        }

        return message;
    }
}
