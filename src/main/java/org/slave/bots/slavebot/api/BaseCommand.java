package org.slave.bots.slavebot.api;

import org.jibble.pircbot.PircBot;

/**
 * Created by Master801 on 12/25/2015 at 2:16 PM.
 *
 * @author Master801
 */
public interface BaseCommand {

    boolean isNameCaseSensitive();

    /**
     * @param instance The instance of the bot using the command
     * @param channel The channel the command was used in
     * @param sender The sender's name
     * @param login The sender's name
     * @param hostname The sender's hostname
     * @param completeLine The complete line of command without the command's name
     * @param parameters The parameters for the command
     *
     * @throws CommandException
     */
    void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException;

    /**
     * How to use the command
     *
     * ${COMMAND_NAME}
     */
    String getUsage();

}
