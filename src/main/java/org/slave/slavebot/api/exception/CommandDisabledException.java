package org.slave.slavebot.api.exception;

/**
 * Created by Master801 on 12/26/2015 at 10:04 AM.
 *
 * @author Master801
 */
public final class CommandDisabledException extends CommandException {

    private static final long serialVersionUID = 8546507024721975424L;

    private final String commandName;

    public CommandDisabledException(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
