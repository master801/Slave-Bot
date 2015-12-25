package org.slave.slavebot.api.exception;

/**
 * Created by Master801 on 12/25/2015 at 2:25 PM.
 *
 * @author Master801
 */
public abstract class CommandException extends Exception {

    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
