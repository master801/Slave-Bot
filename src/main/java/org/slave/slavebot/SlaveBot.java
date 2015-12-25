package org.slave.slavebot;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.exception.CommandNotFoundException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException.ArgumentType;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.SubCommandNotFoundException;
import org.slave.slavebot.commands.CommandAbout;
import org.slave.slavebot.commands.CommandJoinChannel;
import org.slave.slavebot.commands.CommandMail;
import org.slave.slavebot.commands.CommandMessage;
import org.slave.slavebot.commands.CommandPartChannel;
import org.slave.slavebot.commands.CommandReconnect;
import org.slave.slavebot.commands.CommandStop;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;

import java.util.ArrayList;

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
                CommandMessage.INSTANCE,
                CommandMail.INSTANCE,
                new CommandUsage(),
                new CommandCommands()
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

    public void doCommand(final String channel, final String sender, final String login, final String hostname, final String message) {
        final String commandNameInMessage = message.substring(1, message.indexOf(' ') != -1 ? message.indexOf(' ') : message.length());
        String completeLine = message.substring(message.indexOf(commandNameInMessage));
        String[] parameters = completeLine.split(" ");

        Command command = null;
        for(final Command iteratingCommand : commands) {
            for(final String commandName : iteratingCommand.getCommandNames()) {
                if (iteratingCommand.isNameCaseSensitive() ? commandNameInMessage.equals(commandName) : commandNameInMessage.equalsIgnoreCase(commandName)) {
                    command = iteratingCommand;
                    break;
                }
            }
        }
        if (command == null) {
            sendMessage(channel, Colors.RED + (sender + ": ") + "Found no such command!");
            return;
        }
        if (!ArrayHelper.isNullOrEmpty(command.getSubCommands())) {
            final String subCommandName = parameters[0];
            SubCommand subCommand = null;
            for(SubCommand iteratingSubCommand : command.getSubCommands()) {
                if (iteratingSubCommand.getSubCommandName().equalsIgnoreCase(subCommandName)) {
                    subCommand = iteratingSubCommand;
                    break;
                }
            }
            if (subCommand != null) {
                try {
                    completeLine = completeLine.substring(completeLine.indexOf(parameters[0]));

                    String[] cache = new String[parameters.length - 1];
                    System.arraycopy(parameters, 1, cache, 0, cache.length);

                    subCommand.doCommand(this, channel, sender, login, hostname, completeLine, parameters);
                } catch(CommandException e) {
                    if (e instanceof IllegalCommandArgumentException) {
                        switch(((IllegalCommandArgumentException) e).getArgumentType()) {
                            case TOO_MANY:
                                sendMessage(channel, (sender + ": ") + "Too many parameters for this sub-command!");
                                break;
                            case NOT_ENOUGH:
                                sendMessage(channel, (sender + ": ") + "Not enough parameters for this sub-command!");
                                break;
                        }
                    }
                    if (e instanceof CommandNotFoundException) {
                        sendMessage(channel, (sender + ": ") + String.format("Did not find command \"%s\"!", ((CommandNotFoundException)e).getCommandName()));
                    }
                    if (e instanceof SubCommandNotFoundException) {
                        SubCommandNotFoundException subCommandNotFoundException = (SubCommandNotFoundException)e;
                        sendMessage(channel, (sender + ": ") + String.format("Did not find sub-command \"%s\" from command \"%s\"!", subCommandNotFoundException.getSubCommandName(), subCommandNotFoundException.getCommandName()));
                    }
                }
            } else {
                sendMessage(channel, Colors.RED + (sender + ": ") + "Found no such sub-command!");
            }
        } else {
            try {
                command.doCommand(this, channel, sender, login, hostname, completeLine, parameters);
            } catch(CommandException e) {
                if (e instanceof IllegalCommandArgumentException) {
                    switch(((IllegalCommandArgumentException) e).getArgumentType()) {
                        case TOO_MANY:
                            sendMessage(channel, (sender + ": ") + "Too many parameters for this command!");
                            break;
                        case NOT_ENOUGH:
                            sendMessage(channel, (sender + ": ") + "Not enough parameters for this command!");
                            break;
                    }
                }
                if (e instanceof CommandNotFoundException) {
                    sendMessage(channel, (sender + ": ") + String.format("Did not find command \"%s\"!", ((CommandNotFoundException)e).getCommandName()));
                }
            }
        }
    }

    public static String getOwnerName() {
        return Settings.INSTANCE.getOwner();
    }

    private final class CommandUsage implements Command {

        @Override
        public String[] getCommandNames() {
            return new String[] {
                    "usage"
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
        public void doCommand(final PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
            if (ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);
            if (parameters.length > 2) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);


            Command command = null;

            ITERATING:
            for(final Command iteratingCommand : SlaveBot.this.commands) {
                for(final String iteratingCommandName : iteratingCommand.getCommandNames()) {
                    if (iteratingCommand.isNameCaseSensitive() ? iteratingCommandName.equals(parameters[0]) : iteratingCommandName.equalsIgnoreCase(parameters[0])) {
                        command = iteratingCommand;
                        break ITERATING;
                    }
                }
            }

            if (command != null) {
                switch(parameters.length) {
                    case 1:
                        sendMessage(channel, (sender + ": ") + command.getUsage());
                        break;
                    case 2:
                        break;
                }
            }

            switch(parameters.length) {
                case 1:
                    throw new CommandNotFoundException(parameters[0]);
                case 2:
                    throw new SubCommandNotFoundException(parameters[0], parameters[1]);
            }
        }

        @Override
        public String getUsage() {
            return "Use \"!${COMMAND_NAME} A_COMMAND_NAME\" to see the usage of the command.";
        }

    }

    private final class CommandCommands implements Command {

        private String allCommandNames = null;

        @Override
        public String[] getCommandNames() {
            return new String[] {
                    "cmds",
                    "commands"
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
        public void doCommand(final PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
            if (!ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);
            if (StringHelper.isNullOrEmpty(allCommandNames)) rebuild();
            sendMessage(channel, (sender + ": ") + "All commands: \"" + allCommandNames + "\"");
        }

        private void rebuild() {
            ArrayList<String> allCommandNames = new ArrayList<>();
            for(Command command : SlaveBot.this.commands) {
                String cache = "";
                cache += "[";
                cache += Joiner.on(", ").join(command.getCommandNames());
                cache += "]";

                allCommandNames.add(cache);
            }
            CommandCommands.this.allCommandNames = Joiner.on(' ').join(allCommandNames);
        }

        @Override
        public String getUsage() {
            return "Use \"!${COMMAND_NAME}\" to show all commands.";
        }

    }

}
