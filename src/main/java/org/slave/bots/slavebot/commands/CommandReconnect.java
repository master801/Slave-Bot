package org.slave.bots.slavebot.commands;

import org.jibble.pircbot.PircBot;
import org.slave.bots.slavebot.api.Command;

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
    public boolean isCommandNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String[] parameters) {
        //FIXME
        instance.sendMessage(channel, (sender + ": ") + "Command is disabled. Sorry about that!");
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
