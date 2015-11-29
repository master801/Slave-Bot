package org.slave.bots.slavebot.commands;

import com.google.common.base.Joiner;
import org.jibble.pircbot.PircBot;
import org.slave.bots.slavebot.SlaveBot;
import org.slave.bots.slavebot.api.Command;
import org.slave.lib.helpers.ArrayHelper;

/**
 * Created by Master801 on 11/29/2015 at 10:09 AM.
 *
 * @author Master801
 */
public final class CommandMessage implements Command {

    public static final Command INSTANCE = new CommandMessage();

    private CommandMessage() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "msg",
                "message"
        };
    }

    @Override
    public boolean isCommandNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String[] parameters) {
        if (sender.equals(SlaveBot.getOwnerName()) && login.equals("~" + SlaveBot.getOwnerName())) {
            if (!ArrayHelper.isNullOrEmpty(parameters) && parameters.length >= 2) {
                String[] newParameters = new String[parameters.length - 1];
                System.arraycopy(parameters, 1, newParameters, 0, newParameters.length);

                instance.sendMessage(parameters[0], Joiner.on(' ').join(newParameters));
            } else {
                instance.sendMessage(channel, (sender + ": ") + "Found no user to message to, or no message at all!");
            }
        }
    }

    @Override
    public String getUsage() {
        return "Use \"!${COMMAND_NAME} SOMEONE A_MESSAGE\" to message a user. Only my owner may use this command.";
    }

}
