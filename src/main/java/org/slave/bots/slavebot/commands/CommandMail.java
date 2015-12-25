package org.slave.bots.slavebot.commands;

import com.google.common.base.Joiner;
import org.jibble.pircbot.PircBot;
import org.slave.bots.slavebot.SlaveBot;
import org.slave.bots.slavebot.api.Command;
import org.slave.bots.slavebot.api.CommandException;
import org.slave.bots.slavebot.api.SubCommand;
import org.slave.bots.slavebot.resources.Message;
import org.slave.lib.helpers.ArrayHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Master801 on 12/24/2015 at 10:17 AM.
 *
 * @author Master801
 */
public final class CommandMail implements Command {

    public static final Command INSTANCE = new CommandMail();

    private static final File MAIL_DIRECTORY = new File("mail");

    private CommandMail() {
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "mail"
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void doCommand(final PircBot instance, final String channel, final String sender, final String login, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        if (!CommandMail.MAIL_DIRECTORY.exists()) CommandMail.MAIL_DIRECTORY.mkdir();

        File userMailDir = new File(CommandMail.MAIL_DIRECTORY, sender);
        if (parameters.length == 1 && parameters[0].equalsIgnoreCase("")) {//Assume someone is checking their mail
            if (!userMailDir.exists() || ArrayHelper.isNullOrEmpty(userMailDir.list())) {
                userMailDir.mkdir();
                instance.sendMessage(channel, (sender + ": ") + "No mail!");
            } else {
                File[] files = userMailDir.listFiles();
                if (!ArrayHelper.isNullOrEmpty(files)) {
                    ArrayList<Message> messages = new ArrayList<>();
                    for(File file : files) {
                        if (!file.getName().endsWith(".message")) continue;
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                            try {
                                messages.add((Message)objectInputStream.readObject());
                            } catch(ClassNotFoundException e) {
                                SlaveBot.SLAVE_BOT_LOGGER.catching(e);
                            }
                            objectInputStream.close();
                            fileInputStream.close();
                        } catch(IOException e) {
                            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
                        }
                    }
                    for(Message message : messages) instance.sendMessage(sender, String.format("From: \"%s\", ID: \"%s\", Message: \"%s\"", message.getSender(), message.getUUID(), message.getMessage()));
                }
            }
            return;
        }
        if (parameters.length < 2) {
            instance.sendMessage(channel, (sender + ": ") + "No person or message specified!");
            return;
        }


        if (parameters[0].equalsIgnoreCase("delete")) {//Delete sub-command
            final String id = parameters[1];
            if (id.equalsIgnoreCase("all")) {
                File[] files = userMailDir.listFiles();
                if (!ArrayHelper.isNullOrEmpty(files)) {
                    for(File file : files) file.delete();
                }
                return;
            }
            try {
                File[] files = userMailDir.listFiles();
                if (!ArrayHelper.isNullOrEmpty(files)) {
                    for(File file : files) {
                        boolean equal = false;
                        if (!file.getName().endsWith(".message")) continue;
                        FileInputStream fileInputStream = new FileInputStream(file);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                        try {
                            Message message = (Message)objectInputStream.readObject();
                            if (id.equals(message.getUUID())) equal = true;
                        } catch(ClassNotFoundException e) {
                            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
                        }

                        objectInputStream.close();
                        fileInputStream.close();

                        if (equal) {
                            file.delete();
                            break;
                        }
                    }
                }
            } catch(IOException e) {
                SlaveBot.SLAVE_BOT_LOGGER.catching(e);
            }
            return;
        }
        if (!userMailDir.exists()) userMailDir.mkdir();



        String[] choppedStringMessage = new String[parameters.length - 1];
        System.arraycopy(parameters, 1, choppedStringMessage, 0, choppedStringMessage.length);

        Message message = new Message(UUID.randomUUID().toString(), sender, parameters[1], Joiner.on(' ').join(choppedStringMessage));

        try {
            int index = 0;

            if (!ArrayHelper.isNullOrEmpty(userMailDir.list())) {
                while(true) {
                    File file = new File(userMailDir, index + ".message");
                    if (file.exists()) {
                        index++;
                    } else {
                        break;
                    }
                }
            }

            File messageFile = new File(userMailDir, index + ".message");

            FileOutputStream fileOutputStream = new FileOutputStream(messageFile);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(message);
            objectOutputStream.close();

            fileOutputStream.close();
        } catch(IOException e) {
            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
        }
    }

    @Override
    public String getUsage() {
        return "Use \"!mail PERSON MESSAGE\" to leave them a message next time they are in the channel.\n" +
                "Use \"!mail\" to check if there are any messages for you.\n" +
                "Use \"!mail delete MESSAGE_UUID\" to delete a message.\n" +
                "Use \"!mail delete all\" to delete all messages.";
    }

}
