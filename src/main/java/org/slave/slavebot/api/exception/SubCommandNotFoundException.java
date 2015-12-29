package org.slave.slavebot.api.exception;

/**
 * Created by Master801 on 12/25/2015 at 2:55 PM.
 *
 * @author Master801
 */
public final class SubCommandNotFoundException extends CommandException {

    private static final long serialVersionUID = 1985383576486031253L;

    private final String commandName, subCommandName;

    public SubCommandNotFoundException(final String commandName, final String subCommandName) {
        this.commandName = commandName;
        this.subCommandName = subCommandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getSubCommandName() {
        return subCommandName;
    }

}
