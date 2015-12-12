package org.slave.bots.slavebot.commands;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.slave.bots.slavebot.SlaveBot;
import org.slave.bots.slavebot.api.Command;
import org.slave.bots.slavebot.threads.ThreadConsoleInput;

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
    public boolean isCommandNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(PircBot instance, final String channel, final String sender, final String login, final String hostname, final String[] parameters) {
        if (sender.equals(SlaveBot.getOwnerName()) && login.equals("~" + SlaveBot.getOwnerName())) {
            SlaveBot.SLAVE_BOT_LOGGER.info("Requested shutdown from owner. Channel: \"{}\", NickName: \"{}\", Name: \"{}\", HostName: \"{}\"", channel, sender, login, hostname);
            instance.quitServer();
            ThreadConsoleInput.stopThread();
            try {
                Thread.sleep(60 * 1000);
            } catch(Exception e) {
                SlaveBot.SLAVE_BOT_LOGGER.catching(e);
            }
            System.exit(0);
        } else {
            instance.sendMessage(channel, Colors.RED + (sender + ": ") + "You may not use this command! You are not my owner!");
        }
    }

    @Override
    public String getUsage() {
        return "Use \"!stop\" to stop the bot. Only my owner may use this command.";
    }

}
