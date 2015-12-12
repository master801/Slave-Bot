package org.slave.bots.slavebot;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jibble.pircbot.*;
import org.slave.bots.slavebot.api.Command;
import org.slave.bots.slavebot.commands.*;
import org.slave.lib.helpers.StringHelper;

/**
 * Created by Master801 on 11/29/2015 at 7:14 AM.
 *
 * @author Master801
 */
public final class SlaveBot extends PircBot {

    public static final Logger SLAVE_BOT_LOGGER = LogManager.getLogger("Slave-Bot");

    private final Command[] commands;

    public SlaveBot() {
        setName(Settings.INSTANCE.getNick());
        setLogin(Settings.INSTANCE.getName());
        setVersion(Main.VERSION);
        setMessageDelay((long)0);
        commands = new Command[] {
                CommandAbout.INSTANCE,
                CommandStop.INSTANCE,
                CommandReconnect.INSTANCE,
                CommandJoinChannel.INSTANCE,
                CommandPartChannel.INSTANCE,
                CommandChat.INSTANCE,
                CommandMessage.INSTANCE
        };
    }

    @Override
    protected void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        if (StringHelper.isNullOrEmpty(message)) return;
        if (message.startsWith(Settings.INSTANCE.getCommandFlag())) {//Command flag
            doCommand(channel, sender, login, hostname, message);
            return;
        }
        if (message.contains(getName())) onPrivateMessage(sender, login, hostname, message);
    }

    @Override
    protected void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
        sendMessage(SlaveBot.getOwnerName(), Colors.DARK_BLUE + String.format("Received a PM from \"%s|%s\" from hostname \"%s\".", sender, login, hostname));//Delegate messages from the bot to the owner
        sendMessage(SlaveBot.getOwnerName(), message);
    }

    public void doCommand(final String channel, final String sender, final String login, final String hostname, String message) {
        final String messageCommandName = message.substring(1, message.indexOf(' ') != -1 ? message.indexOf(' ') : message.length());
        final String[] parameters = message.substring(message.indexOf(' ') != -1 ? message.indexOf(' ') + 1 : messageCommandName.length() + 1).split(" ");

        String recipient = null;
        String messageToSend = null;
        if (messageCommandName.equalsIgnoreCase("usage")) {
            boolean foundCommandNameParameter = false;
            for(Command command : commands) {
                for(String commandName : command.getCommandNames()) {
                    if (command.isCommandNameCaseSensitive() ? commandName.equals(parameters[0]) : commandName.equalsIgnoreCase(parameters[0])) {
                        if (!StringHelper.isNullOrEmpty(command.getUsage())) {
                            recipient = channel;
                            messageToSend = (sender != null ? (Colors.CYAN + sender + ": ") : "") + (Colors.DARK_GREEN + "Usage: \"" + command.getUsage().replace("${COMMAND_NAME}", commandName) + "\"");
                        } else {
                            recipient = channel;
                            messageToSend = (sender != null ? (Colors.CYAN + sender + ": ") : "") + (Colors.DARK_GREEN + "Sorry, no usage is available for this command.");
                        }
                        foundCommandNameParameter = true;
                        break;
                    }
                }
                if (foundCommandNameParameter) break;
            }
            if (!foundCommandNameParameter) {
                recipient = channel;
                messageToSend = (sender != null ? (sender + ": ") : "") + "Incorrect usage or the command may not exist! Usage: \"!usage COMMAND_NAME\"";
            }
        }
        if ((messageCommandName.equalsIgnoreCase("cmds") || messageCommandName.equalsIgnoreCase("commands")) && (recipient == null && messageToSend == null)) {
            String commandsString = "";

            for(int i = 0; i < commands.length; ++i) {
                Command command = commands[i];
                commandsString += "[";
                commandsString += Joiner.on(", ").join(command.getCommandNames());
                commandsString += (i == commands.length - 1 ? "]" : "] ");
            }

            recipient = channel;
            messageToSend = String.format((sender != null ? (sender + ": ") : "") + Colors.DARK_BLUE + "Commands: \"%s\"", commandsString);
        }
        if (recipient != null && messageToSend != null) {//Console sent command
            SlaveBot.SLAVE_BOT_LOGGER.info(messageToSend);
            return;
        }

        boolean foundCommand = false;
        for(final Command command : commands) {
            for(final String commandName : command.getCommandNames()) {
                if (command.isCommandNameCaseSensitive() ? messageCommandName.equals(commandName) : messageCommandName.equalsIgnoreCase(commandName)) {
                    command.doCommand(SlaveBot.this, channel, sender, login, hostname, parameters);
                    foundCommand = true;
                    break;
                }
            }
        }
        if (!foundCommand) sendMessage(channel, Colors.RED + (sender + ": ") + "Found no such command!");
    }

    public static String getOwnerName() {
        return Settings.INSTANCE.getOwner();
    }

}
