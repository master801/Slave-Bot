package org.slave.bots.slavebot.api;

import org.jibble.pircbot.PircBot;

/**
 * Created by Master801 on 11/29/2015 at 8:06 AM.
 *
 * @author Master801
 */
public interface Command {

    String[] getCommandNames();

    boolean isCommandNameCaseSensitive();

    void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String[] parameters);

    /**
     * ${COMMAND_NAME}
     */
    String getUsage();

}
