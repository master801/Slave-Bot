package org.slave.slavebot.commands;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.SubCommand;
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
    public SubCommand[] getSubCommands() {
        return null;
    }


    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        if (channel == null || sender == null || login == null || hostname == null) return;
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
