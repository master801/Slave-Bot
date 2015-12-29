package org.slave.slavebot.api;

/**
 * Created by Master801 on 11/29/2015 at 8:06 AM.
 *
 * @author Master801
 */
public interface Command extends BaseCommand {

    String[] getCommandNames();

    /**
     * Note, there must be <b>NO</b> default usage of this command if sub-commands are the be used.
     *
     * @return Sub-commands for this command. Return null or an empty array if there are no sub-command available.
     */
    SubCommand[] getSubCommands();

    /**
     * @return If the command has any sub-commands
     */
    boolean hasSubCommands();

}
