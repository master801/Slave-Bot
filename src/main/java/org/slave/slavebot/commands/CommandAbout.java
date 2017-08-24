package org.slave.slavebot.commands;

import org.jibble.pircbot.Colors;
import org.slave.slavebot.SlaveBot;
import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 11/29/2015 at 8:06 AM.
 *
 * @author Master801
 */
public final class CommandAbout implements Command {

    public static final Command INSTANCE = new CommandAbout();

    private CommandAbout() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "about"
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
    public void init() {
        //NOOP
    }

    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(final Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        final String description = "I am a bot created by Master 801 with PircBot. I have very little functionality, so please refrain from talking to me.";
        if (channel == null || senderNickName == null || hostname == null) {
            SlaveBot.SLAVE_BOT_LOGGER.info(description);
            return;
        }
        instance.sendMessage(channel, (senderNickName + ": ") + Colors.BLUE + description);
    }

    @Override
    public String getUsage() {
        return "Use \"!${COMMAND_NAME}\" to show the info for the bot.";
    }

}
