package org.slave.slavebot.commands;

import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 11/29/2015 at 10:09 AM.
 *
 * @author Master801
 */
public final class CommandMessage implements Command {

    public static final Command INSTANCE = new CommandMessage();

    private final SubCommand[] subCommands;

    private CommandMessage() {
        subCommands = new SubCommand[] {
                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "channel";
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
                    public void doCommand(final Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                },
                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "user";
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
                    public void doCommand(final Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                }
        };
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "msg",
                "message"
        };
    }

    @Override
    public SubCommand[] getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean hasSubCommands() {
        return true;
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
        //NOOP
    }

    @Override
    public String getUsage() {
        return "";
    }

}
