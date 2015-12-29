package org.slave.slavebot.api;

import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 12/25/2015 at 2:16 PM.
 *
 * @author Master801
 */
public interface BaseCommand {

    boolean isNameCaseSensitive();

    /**
     * @param instance The instance of the bot using the (sub-)command {@link org.slave.slavebot.api.Bot}
     * @param channel The channel the (sub-)command was used in
     * @param senderNickName The sender's nick-name
     * @param hostname The sender's hostname
     * @param completeLine The complete line of (sub-)command without the (sub-)command's name
     * @param parameters The parameters for the (sub-)command
     *
     * @throws CommandException
     */
    void doCommand(final Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException;

    /**
     * <p>
     *     {@link org.slave.slavebot.api.Command}:
     *     ${COMMAND_NAME}
     * </p>
     *
     * <p>
     *     {@link org.slave.slavebot.api.SubCommand}:
     *     ${SUB-COMMAND_NAME}
     * </p>
     */
    String getUsage();

}
