package com.caversia.plugins.votes.spigot.utils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.caversia.plugins.votes.spigot.model.VoteInfo;
import com.caversia.plugins.votes.spigot.utils.messages.MessagePlaceHoldersResolver;
import com.caversia.plugins.votes.spigot.utils.messages.MessageUtils;
import com.caversia.plugins.votes.spigot.utils.messages.StaticMessagePlaceHoldersResolver;

/**
 * Provides utilities methods to deal with rewards.
 * 
 * @author amartins
 */
public enum RewardsUtils {
    INSTANCE;

    private Random rand = new Random();

    /**
     * Processes a received vote.
     * 
     * @param votesInfo the vote info
     */
    public void processVote(VoteInfo votesInfo) {
        if (!processPermissionsRewards(votesInfo)) {
            processDefaultRewards(votesInfo);
        }

        processCumulativeRewards(votesInfo);
        processLuckyRewards(votesInfo);
    }

    /**
     * Processes default rewards.
     * 
     * @param votesInfo the vote info
     */
    private void processDefaultRewards(VoteInfo votesInfo) {
        giveRewards(Config.INSTANCE.getDefaultRewardsMessages(), Config.INSTANCE.getDefaultRewardsCommands(), votesInfo);
    }

    /**
     * Processes cumulative rewards.
     * 
     * @param votesInfo the vote info
     */
    private void processCumulativeRewards(VoteInfo votesInfo) {
        //@formatter:off
        Config.INSTANCE.getCumulativeRewardsTiers()
                       .stream()
                       .filter( t -> votesInfo.getNumberOfVotes() == t)
                       .forEach(t -> giveRewards( Config.INSTANCE.getCumulativeRewardsMessages(t),
                                                  Config.INSTANCE.getCumulativeRewardsCommands(t), 
                                                  votesInfo));
        //@formatter:on
    }

    /**
     * Processes rewards given when players have specific permissions.
     * 
     * @param votesInfo the vote info
     * @return <strong>true</strong> if a reward was given, <strong>false</strong> otherwise.
     */
    private boolean processPermissionsRewards(VoteInfo votesInfo) {

        AtomicBoolean receivedRewards = new AtomicBoolean(false);

        //@formatter:off
        Set<String> permissionsRewardsTiers = Config.INSTANCE.getPermissionsRewardsTiers();
        permissionsRewardsTiers.stream()
                       .filter(votesInfo.getPlayer()::hasPermission)
                       .peek(p -> Logger.info("Player has permission: {}", p))
                       .peek(v -> receivedRewards.set(true))
                       .forEach(t -> giveRewards( Config.INSTANCE.getPermissionsRewardsMessages(t),
                                                  Config.INSTANCE.getPermissionsRewardsCommands(t), 
                                                  votesInfo));
        //@formatter:on

        return receivedRewards.get();
    }

    /**
     * Process lucky rewards.
     * 
     * @param votesInfo the vote info
     */
    private void processLuckyRewards(VoteInfo votesInfo) {
        //@formatter:off
        Config.INSTANCE.getLuckyRewardsTiers()
                       .stream()
                       .filter( t -> randInt(1, t) == t)
                       .forEach(t -> giveRewards( Config.INSTANCE.getLuckyRewardsMessages(t),
                                                  Config.INSTANCE.getLuckyRewardsCommands(t), 
                                                  votesInfo));
        //@formatter:on
    }

    /**
     * Gives rewards to the player by executing the provided commands.
     * 
     * @param messages the messages to broadcast during the reward
     * @param commands the commands to execute as rewards
     * @param votesInfo the vote info
     */
    private void giveRewards(Map<String, List<String>> messages, List<String> commands, VoteInfo votesInfo) {
        if (!votesInfo.isQueuedVote()) {
            MessageUtils.dispatch(messages,
                    new StaticMessagePlaceHoldersResolver(votesInfo.getPlayer(), votesInfo.getNumberOfVotes()));
        }

        dispatchCommands(commands, votesInfo);
    }

    /**
     * Dispatch the reward commands.
     * 
     * @param commands the commands to dispatch
     * @param votesInfo the vote info
     */
    private void dispatchCommands(List<String> commands, VoteInfo votesInfo) {
        for (String command : commands) {
            CommandSender cmdSender = command.startsWith("/") ? votesInfo.getPlayer() : Bukkit.getConsoleSender();
            Bukkit.getServer().dispatchCommand(cmdSender, enrichCommand(command, votesInfo));
        }
    }

    /**
     * Generates a random number on a given interval.
     * 
     * @param min the lower end of the interval
     * @param max the high end of the interval
     * @return the random number.
     */
    private int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Enriches a command by filling its placeholders with info provided on a {@link MessagePlaceHoldersResolver}.
     * 
     * @param command the command to enrich
     * @param info the info to use on the placeholders
     * @return the command with the placeholders replaced with the provided info.
     */
    private String enrichCommand(String command, VoteInfo info) {
        if (command.contains("%PlayerName%")) {
            command = command.replaceAll("%PlayerName%", info.getPlayer().getName());
        }

        return command;
    }
}
