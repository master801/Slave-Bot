package org.slave.bots.slavebot.commands;

import com.google.common.base.Joiner;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slave.bots.slavebot.api.Command;
import org.slave.lib.helpers.ArrayHelper;

/**
 * Created by Master801 on 11/29/2015 at 9:28 AM.
 *
 * @author Master801
 */
public final class CommandChat implements Command {

    public static final Command INSTANCE = new CommandChat();

    private CommandChat() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "chat"
        };
    }

    @Override
    public boolean isCommandNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, String channel, String sender, String login, String hostname, String[] parameters) {
        for(User user : instance.getUsers(channel)) {
            if (user.isOp() && user.getNick().equals(sender)) {
                if (!ArrayHelper.isNullOrEmpty(parameters)) {
                    if (parameters[0].startsWith("#")) {//Channel
                        String[] newParameters = new String[parameters.length - 1];
                        System.arraycopy(parameters, 1, newParameters, 0, newParameters.length);
                        instance.sendMessage(parameters[0], Joiner.on(' ').join(newParameters));
                        break;
                    }
                    instance.sendMessage(channel, Joiner.on(' ').join(parameters));
                }
                break;
            }
        }
    }

    @Override
    public String getUsage() {
        return "Sends a chat message. Usage: \"!${COMMAND_NAME} MESSAGE\"";
    }

}
