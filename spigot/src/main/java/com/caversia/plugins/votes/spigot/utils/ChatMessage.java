/**
 * 
 */
package com.caversia.plugins.votes.spigot.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Implementation of a builder pattern to create and send chat messages to a player.
 * 
 * @author amartins
 */
public class ChatMessage {

    private StringBuilder message = new StringBuilder();

    private Player player;

    /**
     * Builder factory method.
     * 
     * @param player the player to whom the message will be sent
     * @return a new instance of the builder
     */
    public static ChatMessage to(Player player) {
        return new ChatMessage(player);
    }

    /**
     * Constructor.
     * 
     * @param player the player to whom the message will be sent
     */
    private ChatMessage(Player player) {
        this.player = player;
    }

    /**
     * Builds and send the message to the player.
     */
    public void send() {
        player.sendMessage(message.toString());
    }

    /**
     * Sets the {@link ChatColor#BLACK} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage black(Object... content) {
        return write(ChatColor.BLACK, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_BLUE} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkBlue(Object... content) {
        return write(ChatColor.DARK_BLUE, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_GREEN} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkGreen(Object... content) {
        return write(ChatColor.DARK_GREEN, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_AQUA} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkAqua(Object... content) {
        return write(ChatColor.DARK_AQUA, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_RED} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkRed(Object... content) {
        return write(ChatColor.DARK_RED, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_PURPLE} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkPurple(Object... content) {
        return write(ChatColor.DARK_PURPLE, content);
    }

    /**
     * Sets the {@link ChatColor#GOLD} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage gold(Object... content) {
        return write(ChatColor.GOLD, content);
    }

    /**
     * Sets the {@link ChatColor#GRAY} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage gray(Object... content) {
        return write(ChatColor.GRAY, content);
    }

    /**
     * Sets the {@link ChatColor#DARK_GRAY} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage darkGray(Object... content) {
        return write(ChatColor.DARK_GRAY, content);
    }

    /**
     * Sets the {@link ChatColor#BLUE} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage blue(Object... content) {
        return write(ChatColor.BLUE, content);
    }

    /**
     * Sets the {@link ChatColor#GREEN} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage green(Object... content) {
        return write(ChatColor.GREEN, content);
    }

    /**
     * Sets the {@link ChatColor#AQUA} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage aqua(Object... content) {
        return write(ChatColor.AQUA, content);
    }

    /**
     * Sets the {@link ChatColor#RED} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage red(Object... content) {
        return write(ChatColor.RED, content);
    }

    /**
     * Sets the {@link ChatColor#LIGHT_PURPLE} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage lPurple(Object... content) {
        return write(ChatColor.LIGHT_PURPLE, content);
    }

    /**
     * Sets the {@link ChatColor#YELLOW} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage yellow(Object... content) {
        return write(ChatColor.YELLOW, content);
    }

    /**
     * Sets the {@link ChatColor#WHITE} color and append content to the message.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage white(Object... content) {
        return write(ChatColor.WHITE, content);
    }

    /**
     * Sets magical characters that change around randomly.
     * 
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage magic(Object... content) {
        return write(ChatColor.MAGIC, content);
    }

    /**
     * Makes the text bold.
     * 
     * @return this builder instance
     */
    public ChatMessage bold() {
        return write(ChatColor.BOLD);
    }

    /**
     * Makes a line appear through the text.
     * 
     * @return this builder instance
     */
    public ChatMessage strikethrough() {
        return write(ChatColor.STRIKETHROUGH);
    }

    /**
     * Makes the text appear underlined.
     * 
     * @return this builder instance
     */
    public ChatMessage underline() {
        return write(ChatColor.UNDERLINE);
    }

    /**
     * Makes the text italic.
     * 
     * @return this builder instance
     */
    public ChatMessage italic() {
        return write(ChatColor.ITALIC);
    }

    /**
     * Resets all previous chat colors or formats.
     * 
     * @return this builder instance
     */
    public ChatMessage reset() {
        return write(ChatColor.RESET);
    }

    /**
     * Appends content to the message.
     * 
     * @param content the content to append
     * @return this builder instance
     */
    public ChatMessage write(Object content) {
        message.append(content);
        return this;
    }

    /**
     * Sets a provided {@link ChatColor} color and append content to the message.
     * 
     * @param color the {@link ChatColor} to set
     * @param content the content to append to the message
     * @return this builder instance
     */
    public ChatMessage write(ChatColor color, Object... content) {
        message.append(color);
        for (Object contentEntry : content) {
            message.append(contentEntry);
        }

        return this;
    }
}
