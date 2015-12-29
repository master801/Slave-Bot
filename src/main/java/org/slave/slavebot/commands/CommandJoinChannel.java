package org.slave.slavebot.commands;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slave.lib.helpers.StringHelper;
import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;

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
    public SubCommand[] getSubCommands() {
        return null;
    }

    @Override
    public boolean hasSubCommands() {
        return false;
    }


    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        if (channel == null || senderNickName == null || hostname == null) return;
        for(User user : ((PircBot)instance).getUsers(channel)) {
            if (user.getNick().equals(senderNickName) && user.isOp()) {
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
                    instance.joinChannel(channelName, null);
                }
                break;
            }
        }
    }

    @Override
    public String getUsage() {
        //TODO Refactor
        return "Joining a channel: \"!${COMMAND_NAME} #CHANNEL_NAME\"" +
                " " +
                "Joining a channel with a password: \"!${COMMAND_NAME} #CHANNEL_NAME CHANNEL_PASSWORD\"";
    }

}
