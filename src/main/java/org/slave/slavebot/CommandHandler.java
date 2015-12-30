package org.slave.slavebot;

import com.google.common.base.Joiner;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException.ArgumentType;
import org.slave.slavebot.api.exception.NoSubCommandsException;
import org.slave.slavebot.commands.CommandAbout;
import org.slave.slavebot.commands.CommandJoinChannel;
import org.slave.slavebot.commands.CommandMail;
import org.slave.slavebot.commands.CommandMessage;
import org.slave.slavebot.commands.CommandPartChannel;
import org.slave.slavebot.commands.CommandReconnect;
import org.slave.slavebot.commands.CommandStop;

import java.util.ArrayList;

/**
 * Created by Master801 on 12/29/2015 at 5:52 PM.
 *
 * @author Master801
 */
public final class CommandHandler {

    public static final CommandHandler INSTANCE = new CommandHandler();

    private final Object lock = new Object();
    private Command[] commands;

    public void init(final Bot bot) {
        if (bot == null) return;
        synchronized(lock) {
            commands = new Command[] {
                    CommandAbout.INSTANCE,
                    CommandStop.INSTANCE,
                    CommandReconnect.INSTANCE,
                    CommandJoinChannel.INSTANCE,
                    CommandPartChannel.INSTANCE,
                    CommandMessage.INSTANCE,
                    CommandMail.INSTANCE,
                    new Command() {

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
                        public void doCommand(final Bot instance, final String channel, final String sender, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
                            if (ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);
                            if (parameters.length > 2) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);


                            Command command = null;
                            for(final Command iteratingCommand : CommandHandler.this.commands) {
                                for(final String iteratingCommandName : iteratingCommand.getCommandNames()) {
                                    if (iteratingCommand.isNameCaseSensitive() ? iteratingCommandName.equals(parameters[0]) : iteratingCommandName.equalsIgnoreCase(parameters[0])) {
                                        command = iteratingCommand;
                                        break;
                                    }
                                }
                                if (command != null) break;
                            }

                            if (command != null) {
                                String usage = null;
                                switch(parameters.length) {
                                    case 1:
                                        if (!command.hasSubCommands()) usage = command.getUsage();
                                        break;
                                    case 2:
                                        instance.sendMessage(channel, (sender + ": ") + "Sub-commands are disabled for the usage command.");//TODO
                                        return;
                                }
                                if (StringHelper.isNullOrEmpty(usage)) {
                                    instance.sendMessage(channel, (sender + ": ") + "No usage found for this command.");
                                } else {
                                    instance.sendMessage(channel, (sender + ": ") + usage);
                                }
                            }
                        }

                        @Override
                        public String getUsage() {
                            return "Use \"!${COMMAND_NAME} A_COMMAND_NAME\" to see the usage of the command.";
                        }

                    },
                    new Command() {

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
                        public void doCommand(final Bot instance, final String channel, final String sender, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
                            if (!ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);
                            if (StringHelper.isNullOrEmpty(allCommandNames)) rebuild();//Rebuild only once
                            bot.sendMessage(channel, (sender + ": ") + "All commands: \"" + allCommandNames + "\"");
                        }

                        private void rebuild() {
                            ArrayList<String> allCommandNamesList = new ArrayList<>();
                            for(Command command : CommandHandler.this.commands) {
                                String cache = "";
                                cache += "[";
                                cache += Joiner.on(", ").join(command.getCommandNames());
                                cache += "]";

                                allCommandNamesList.add(cache);
                            }
                            allCommandNames = Joiner.on(' ').join(allCommandNamesList);
                        }

                        @Override
                        public String getUsage() {
                            return "Use \"!${COMMAND_NAME}\" to show all commands.";
                        }
                    }
            };

            for(Command command : commands) {
                command.init();
                if (command.hasSubCommands()) {
                    for(SubCommand subCommand : command.getSubCommands()) subCommand.init();
                }
            }
        }
    }

    public void doCommand(final Bot bot, final String channel, final String senderNickName, final String senderHostName, final String commandName, String completeLine) throws CommandException {
        synchronized(lock) {
            Command command = null;
            for(final Command iteratingCommand : commands) {
                for(final String iteratingCommandName : iteratingCommand.getCommandNames()) {
                    if (iteratingCommand.isNameCaseSensitive() ? iteratingCommandName.equals(commandName) : iteratingCommandName.equalsIgnoreCase(commandName)) {
                        command = iteratingCommand;
                        break;
                    }
                }
                if (command != null) break;
            }

            if (command != null) {
                if (command.hasSubCommands()) {
                    SubCommand[] subCommands = command.getSubCommands();//Cache sub-commands, assume these are created as an array in the method
                    if (ArrayHelper.isNullOrEmpty(subCommands)) {
                        throw new NoSubCommandsException();
                    } else {
                        if (StringHelper.isNullOrEmpty(completeLine)) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);
                        final String subCommandName = completeLine.substring(0, completeLine.indexOf(' '));
                        completeLine = completeLine.substring(completeLine.indexOf(subCommandName));
                        for(SubCommand subCommand : subCommands) {
                            if (subCommand.getSubCommandName().equals(subCommandName)) {
                                String[] split = completeLine.split(" ");
                                if (ArrayHelper.isNullOrEmpty(split)) split = null;
                                subCommand.doCommand(bot, channel, senderNickName, senderHostName, completeLine, split);
                                break;
                            }
                        }
                    }
                } else {
                    String[] split = completeLine.split(" ");
                    if (ArrayHelper.isNullOrEmpty(split) || (StringHelper.isNullOrEmpty(split[0]) && split.length == 1)) split = null;
                    command.doCommand(bot, channel, senderNickName, senderHostName, completeLine, split);
                }
            }
        }
    }

}
