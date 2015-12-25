package org.slave.slavebot.api;

/**
 * Created by Master801 on 12/25/2015 at 2:25 PM.
 *
 * @author Master801
 */
public final class CommandNotFoundException extends CommandException {

    private final String commandName;

    public CommandNotFoundException(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}