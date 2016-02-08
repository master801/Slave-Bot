package org.slave.slavebot.commands;

import com.google.common.base.Joiner;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.FileHelper;
import org.slave.slavebot.SlaveBot;
import org.slave.slavebot.api.Bot;
import org.slave.slavebot.api.Command;
import org.slave.slavebot.api.SubCommand;
import org.slave.slavebot.api.exception.CommandException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException;
import org.slave.slavebot.api.exception.IllegalCommandArgumentException.ArgumentType;
import org.slave.slavebot.resources.Letter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Master801 on 12/24/2015 at 10:17 AM.
 *
 * @author Master801
 */
public final class CommandMail implements Command {

    public static final Command INSTANCE = new CommandMail();

    private static final File LETTERS_DIRECTORY = new File("letters");

    private final SubCommand[] subCommands;

    private CommandMail() {
        subCommands = new SubCommand[] {
                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "delete";
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
                        if (ArrayHelper.isNullOrEmpty(parameters) || parameters.length < 1) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);
                        File lettersDir = new File(CommandMail.LETTERS_DIRECTORY, senderNickName);
                        File[] files = lettersDir.listFiles();
                        if (!lettersDir.exists() || ArrayHelper.isNullOrEmpty(files)) {
                            instance.sendMessage(senderNickName, "No letters were sent to you");
                            return;
                        }
                        String uuid = parameters[0];

                        String[] check = uuid.split("-");
                        if ((check.length != 5) || (check[0].length() != 8 || check[1].length() != 4 || check[2].length() != 4 || check[3].length() != 4 || check[4].length() != 12)) {//Manually check given UUID
                            instance.sendMessage(senderNickName, "Invalid letter UUID!");
                            return;
                        }

                        File letterFile = null;
                        if (ArrayHelper.isNullOrEmpty(files)) {
                            for(File file : files) {
                                if (file.getName().substring(0, file.getName().indexOf('.')).equals(uuid)) {
                                    letterFile = file;
                                    break;
                                }
                            }
                        }

                        if (letterFile != null) {
                            try {
                                RandomAccessFile randomAccessFile = new RandomAccessFile(letterFile, FileHelper.MODE_READ);
                                ByteBuffer headerCache = ByteBuffer.allocate(Letter.HEADER.length());
                                randomAccessFile.getChannel().read(headerCache);
                                byte[] headerBytes = Letter.HEADER.getBytes();

                                int i = 0;
                                while(i < headerCache.capacity()) {
                                    if (headerBytes[i] != headerCache.get(i)) {
                                        instance.sendMessage(senderNickName, "Invalid letter!");
                                        return;
                                    }
                                    i++;
                                }
                                randomAccessFile.close();
                            } catch(IOException e) {
                                SlaveBot.SLAVE_BOT_LOGGER.error("Caught an exception while attempting to verify a letter!", e);
                            }

                            letterFile.delete();
                        } else {
                            instance.sendMessage(senderNickName, "Found no letter with that UUID!");
                        }
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                },

                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "send";
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
                        if (ArrayHelper.isNullOrEmpty(parameters) || parameters.length < 2) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);

                        final String recipient = parameters[0];
                        String[] cache = new String[parameters.length - 1];
                        System.arraycopy(parameters, 1, cache, 0, cache.length);
                        Letter letter = new Letter(UUID.randomUUID().toString(), senderNickName, recipient, Joiner.on(' ').join(cache));

                        File recipientLettersDir = new File(CommandMail.LETTERS_DIRECTORY, recipient);
                        if (!recipientLettersDir.exists()) recipientLettersDir.mkdirs();
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(letter.getUUID() + ".letter"));
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                            objectOutputStream.writeObject(letter);

                            objectOutputStream.flush();
                            objectOutputStream.close();
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch(IOException e) {
                            SlaveBot.SLAVE_BOT_LOGGER.error("Caught an exception while writing a letter!", e);
                        }
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                },

                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "read";
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
                        if (ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.NOT_ENOUGH);
                        if (parameters.length > 1) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);

                        File letterUserDir = new File(CommandMail.LETTERS_DIRECTORY, senderNickName);
                        File[] files = letterUserDir.listFiles();
                        if (CommandMail.this.checkLetters(letterUserDir, files)) {
                            instance.sendMessage(senderNickName, "No letters were sent for you.");
                            return;
                        }

                        String uuid = parameters[0];

                        String[] check = uuid.split("-");
                        if ((check.length != 5) || (check[0].length() != 8 || check[1].length() != 4 || check[2].length() != 4 || check[3].length() != 4 || check[4].length() != 12)) {//Manually check given UUID
                            instance.sendMessage(senderNickName, "Invalid letter UUID!");
                            return;
                        }

                        for(File file : files) {
                            if (file.getName().startsWith(uuid)) {
                                try {
                                    FileInputStream fileInputStream = new FileInputStream(file);
                                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                                    Letter letter = (Letter)objectInputStream.readObject();
                                    instance.sendMessage(senderNickName, String.format("Received letter \"%s\" from \"%s\".\nMessage: \"%s\"", letter.getUUID(), letter.getSender(), letter.getMessage()));

                                    objectInputStream.close();
                                    fileInputStream.close();
                                } catch(IOException | ClassNotFoundException e) {
                                    SlaveBot.SLAVE_BOT_LOGGER.error("Caught an exception while reading a letter!", e);
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                },

                new SubCommand() {

                    @Override
                    public String getSubCommandName() {
                        return "letters";
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
                        if (!ArrayHelper.isNullOrEmpty(parameters)) throw new IllegalCommandArgumentException(ArgumentType.TOO_MANY);

                        File letterUserDir = new File(CommandMail.LETTERS_DIRECTORY, senderNickName);
                        File[] files = letterUserDir.listFiles();
                        if (CommandMail.this.checkLetters(letterUserDir, files)) {
                            instance.sendMessage(senderNickName, "No letters are available for you!");
                            return;
                        }

                        ArrayList<String> letters = new ArrayList<>();
                        for(File file : files) letters.add(file.getName().substring(file.getName().indexOf('.')));

                        instance.sendMessage(senderNickName, String.format("%d letters are available%s", letters.size(), letters.isEmpty() ? "" : " \"" + Joiner.on("\", \"").join(letters) + "\""));
                    }

                    @Override
                    public String getUsage() {
                        return null;
                    }

                }
        };
    }

    @Override
    public String[] getCommandNames() {
        return new String[] {
                "mail"
        };
    }

    @Override
    public SubCommand[] getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean hasSubCommands() {
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void init() {
        CommandMail.LETTERS_DIRECTORY.mkdirs();
    }

    @Override
    public boolean isNameCaseSensitive() {
        return false;
    }

    @Override
    public void doCommand(final Bot instance, final String channel, final String senderNickName, final String hostname, final String completeLine, final String[] parameters) throws CommandException {
        //NOOP
    }

    /**
     * @return If the letters dir exists or contains letters
     */
    private boolean checkLetters(File lettersDir, File[] letters) {
        return lettersDir.exists() || !ArrayHelper.isNullOrEmpty(letters);
    }

    @Override
    public String getUsage() {
        return null;
    }

}
