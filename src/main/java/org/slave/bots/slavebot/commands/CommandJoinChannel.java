package org.slave.bots.slavebot.commands;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slave.bots.slavebot.api.Command;
import org.slave.lib.helpers.StringHelper;

/**
 * Created by Master801 on 11/29/2015 at 8:36 AM.
 *
 * @author Master801
 */
public final class CommandJoinChannel implements Command {

    public static final Command INSTANCE = new CommandJoinChannel();

    private CommandJoinChannel() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "join-channel"
        };
    }

    @Override
    public boolean isCommandNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String[] parameters) {
        for(User user : instance.getUsers(channel)) {
            if (user.getNick().equals(sender) && user.isOp()) {
                String channelName, channelKey;

                if (parameters.length == 2) {//Channel name and key
                    channelName = parameters[0];
                    channelKey = parameters[1];
                } else {
                    channelName = parameters[0];
                    channelKey = null;
                }
                if (!channelName.startsWith("#")) channelName = "#" + channelName;

                if (!StringHelper.isNullOrEmpty(channelKey)) {
                    instance.joinChannel(channelName, channelKey);
                } else {
                    instance.joinChannel(channelName);
                }
                break;
            }
        }
    }

    @Override
    public String getUsage() {
        return "Joining a channel: \"!${COMMAND_NAME} #CHANNEL_NAME\"" +
                " " +
                "Joining a channel with a password: \"!${COMMAND_NAME} #CHANNEL_NAME CHANNEL_PASSWORD\"";
    }

}