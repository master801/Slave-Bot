package org.slave.bots.slavebot.commands;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.slave.bots.slavebot.SlaveBot;
import org.slave.bots.slavebot.api.Command;
import org.slave.bots.slavebot.api.CommandException;
import org.slave.bots.slavebot.api.SubCommand;

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
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        final String description = "I am a bot created by Master 801 with PircBot. I have very little functionality, so please refrain from talking to me.";
        if (channel == null || sender == null || login == null || hostname == null) {
            SlaveBot.SLAVE_BOT_LOGGER.info(description);
            return;
        }
        instance.sendMessage(channel, (sender + ": ") + Colors.BLUE + description);
    }

    @Override
    public String getUsage() {
        return "Use \"!${COMMAND_NAME}\" to show the info for the bot.";
    }

}
