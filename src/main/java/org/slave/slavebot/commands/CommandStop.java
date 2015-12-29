package org.slave.slavebot.commands;

import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandDisabledException;
import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 11/29/2015 at 8:22 AM.
 *
 * @author Master801
 */
public final class CommandStop implements Command {

    public static final Command INSTANCE = new CommandStop();

    private CommandStop() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "stop"
        };
    }

    @Override
    public SubCommand[] getSubCommands() {
        return null;
    }

    @Override
    public boolean hasSubCommands() {
        return false;
    }

    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        throw new CommandDisabledException(getCommandNames()[0]);


        //FIXME
        /*
        if (senderNickName.equals(SlaveBot.getOwnerName()) && login.equals("~" + SlaveBot.getOwnerName())) {
            SlaveBot.SLAVE_BOT_LOGGER.info("Requested shutdown from owner. Channel: \"{}\", NickName: \"{}\", Name: \"{}\", HostName: \"{}\"", channel, senderNickName, login, hostname);
            instance.quitServer();
            System.exit(0);
        } else {
            instance.sendMessage(channel, Colors.RED + (senderNickName + ": ") + "You may not use this command! You are not my owner!");
        }
        */
    }

    @Override
    public String getUsage() {
        return "Use \"!stop\" to stop the bot. Only my owner may use this command.";
    }

}
