package org.slave.slavebot.commands;

import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 11/29/2015 at 8:36 AM.
 *
 * @author Master801
 */
public final class CommandPartChannel implements Command {

    public static final Command INSTANCE = new CommandPartChannel();

    private static final String PARTING_REASON = "Master has called me away.";

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "part-channel"
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
    public void init() {
        //NOOP
    }

    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        //TODO Refactor

        /*
        for(User user : ((PircBot)instance).getUsers(channel)) {
            if (user.getNick().equals(senderNickName) && user.isOp()) {
                String channelName = parameters[0];
                if (StringHelper.isNullOrEmpty(channelName)) {
                    instance.sendMessage(channel, (senderNickName + ": ") + Colors.RED + "No channel to leave from was found!");
                    break;
                }
                if (!channelName.startsWith("#")) channelName = "#" + channelName;

                boolean isConnectedToChannel = false;
                for(String iteratingChannelName : instance.getConnectedChannels()) {
                    if (iteratingChannelName.equalsIgnoreCase(channelName)) {
                        isConnectedToChannel = true;
                        break;
                    }
                }
                if (isConnectedToChannel) {
                    ((PircBot)instance).partChannel(channelName, Colors.DARK_BLUE + CommandPartChannel.PARTING_REASON);
                } else {
                    instance.sendMessage(channelName, (senderNickName + ": ") + Colors.RED + "Cannot part from channel if not connected to it already!");
                }
                break;
            }
        }
        */
    }

    @Override
    public String getUsage() {
        return "Use \"!${COMMAND_NAME} #CHANNEL_NAME\" to part from that channel.";
    }

}
