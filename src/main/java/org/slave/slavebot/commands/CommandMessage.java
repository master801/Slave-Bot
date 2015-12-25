package org.slave.slavebot.commands;

import org.jibble.pircbot.PircBot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.SubCommand;

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
    public SubCommand[] getSubCommands() {
        return new SubCommand[] {
                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "channel";
                    }

                    @Override
                    public boolean isNameCaseSensitive() {
                        return false;
                    }

                    @Override
                    public void doCommand(final PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
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
                    public boolean isNameCaseSensitive() {
                        return false;
                    }

                    @Override
                    public void doCommand(final PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                }
        };
    }

    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        //NOOP
    }

    @Override
    public String getUsage() {
        return "";
    }

}
