package org.slave.slavebot;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.slave.lib.helpers.StringHelper;
import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.exception.CommandDisabledException;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.exception.CommandNotFoundException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException;
import org.slave.slavebot.api.exception.NoSubCommandsException;
import org.slave.slavebot.api.exception.SubCommandNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Master801 on 11/29/2015 at 7:14 AM.
 *
 * @author Master801
 */
public final class SlaveBot extends PircBot implements Bot {

    public static final Logger SLAVE_BOT_LOGGER = LoggerFactory.getLogger("Slave-Bot");

    public SlaveBot() {
        setName(Settings.INSTANCE.getNick());
        setLogin(Settings.INSTANCE.getName());
        setVersion(Main.VERSION);
        setMessageDelay((long)0);

        ThreadExecutor.INSTANCE.execute(new Runnable() {

            @Override
            public void run() {
                CommandHandler.INSTANCE.init(SlaveBot.this);
            }

        });
    }

    @Override
    protected void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        onMessage(channel, sender, hostname, message);
    }

    @Override
    public void onMessage(final String channel, final String senderNickName, final String senderHostName, final String message) {
        if (StringHelper.isNullOrEmpty(message)) return;
        if (message.startsWith(Settings.INSTANCE.getCommandFlag())) {//Command flag
            String completeLine = message.substring(message.indexOf(Settings.INSTANCE.getCommandFlag()) + 1);//Remove command flag
            String commandName = completeLine;
            if (commandName.contains(" ")) commandName = commandName.substring(0, commandName.indexOf(' '));
            completeLine = completeLine.substring(commandName.length() + 1);

            try {
                onCommand(channel, senderNickName, senderHostName, commandName, completeLine);
            } catch(CommandException e) {
                if (e instanceof CommandNotFoundException) sendMessage(channel, Colors.RED + (senderNickName + ": ") + String.format("Couldn't find command \"%s\"!", ((CommandNotFoundException)e).getCommandName()));
                if (e instanceof CommandDisabledException) sendMessage(channel, Colors.RED + (senderNickName + ": ") + String.format("Command \"%s\" is disabled!", ((CommandDisabledException)e).getCommandName()));
                if (e instanceof IllegalCommandArgumentException) {
                    IllegalCommandArgumentException illegalCommandArgumentException = (IllegalCommandArgumentException)e;
                    switch(illegalCommandArgumentException.getArgumentType()) {
                        case TOO_MANY:
                            sendMessage(channel, (senderNickName +  ": ") + Colors.RED + "Too many arguments for the command!");
                            break;
                        case NOT_ENOUGH:
                            sendMessage(channel, (senderNickName + ": ") + Colors.RED + "Not enough arguments for the command!");
                            break;
                    }
                }
                if (e instanceof NoSubCommandsException) sendMessage(channel, Colors.RED + (senderNickName + ": ") + "No sub-commands are available for this command!");
                if (e instanceof SubCommandNotFoundException) {
                    SubCommandNotFoundException subCommandNotFoundException = (SubCommandNotFoundException)e;
                    sendMessage(channel, Colors.RED + (senderNickName + ": ") + String.format("Couldn't find sub-command \"%s\" in command \"%s\"!", subCommandNotFoundException.getSubCommandName(), subCommandNotFoundException.getCommandName()));
                }
            }
            return;
        }
        if (message.contains(getName())) onPrivateMessage(senderNickName, senderNickName, senderHostName, message);
    }

    @Override
    public String[] getConnectedChannels() {
        return super.getChannels();
    }

    @Override
    protected void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
        sendMessage(SlaveBot.getOwnerName(), Colors.DARK_BLUE + String.format("Received a PM from \"%s|%s\" from hostname \"%s\".", sender, login, hostname));//Delegate messages from the bot to the owner
        sendMessage(SlaveBot.getOwnerName(), message);
    }

    @Override
    public void onCommand(final String channel, final String senderNickName, final String senderHostName, final String commandName, final String completeLine) throws CommandException {
        /*
        ThreadExecutor.INSTANCE.execute(new Runnable() {

            @Override
            public void run() {
                try {
                */
                    CommandHandler.INSTANCE.doCommand(SlaveBot.this, channel, senderNickName, senderHostName, commandName, completeLine);
        /*
                } catch(CommandException e) {
                    //Ignore?
                }
            }

        });
        */
    }

    public static String getOwnerName() {
        return Settings.INSTANCE.getOwner();
    }

}
