package org.slave.bots.slavebot.api;

/**
 * <p>
 *     Thrown when the command has not found any arguments for it, or when there are more arguments that needed (examples: {@link org.slave.bots.slavebot.commands.CommandStop} {@link org.slave.bots.slavebot.commands.CommandAbout}).
 * </p>
 *
 *
 * Created by Master801 on 12/25/2015 at 1:47 PM.
 *
 * @author Master801
 */
public final class IllegalCommandArgumentException extends CommandException {

    private final ArgumentType argumentType;

    public IllegalCommandArgumentException(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }

    public enum ArgumentType {

        TOO_MANY,

        NOT_ENOUGH;

        ArgumentType() {
        }

    }

}
