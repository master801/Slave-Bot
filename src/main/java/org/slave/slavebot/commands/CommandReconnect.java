package org.slave.slavebot.commands;

import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 11/29/2015 at 8:33 AM.
 *
 * @author Master801
 */
public final class CommandReconnect implements Command {

    public static final Command INSTANCE = new CommandReconnect();

    private CommandReconnect() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "reconnect"
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
        //FIXME
        instance.sendMessage(channel, (senderNickName + ": ") + "Command is disabled. Sorry about that!");
        /*
        for(User user : instance.getUsers(channel)) {
            if (user.getNick().equals(sender) && user.isOp()) {
                try {
                    instance.reconnect();
                } catch(IOException | IrcException e) {
                    SlaveBot.SLAVE_BOT_LOGGER.catching(e);
                    System.exit(-1);
                }
                break;
            }
        }
        */
    }

    @Override
    public String getUsage() {
        return "Reconnects to the current server.";
    }

}
