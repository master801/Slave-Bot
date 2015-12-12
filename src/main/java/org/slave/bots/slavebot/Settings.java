package org.slave.bots.slavebot;

import org.slave.lib.api.CommentProperties;
import org.slave.lib.helpers.ConfigHelper;

import java.io.*;

/**
 * Created by Master801 on 11/29/2015 at 12:26 PM.
 *
 * @author Master801
 */
public final class Settings {

    public static final Settings INSTANCE = new Settings();

    private static final File PROPERTIES_FILE = new File("settings.properties");

    private final CommentProperties configFile = ConfigHelper.createCommentProperties();

    public synchronized void load() throws IOException {
        if (!Settings.PROPERTIES_FILE.exists()) {
            SlaveBot.SLAVE_BOT_LOGGER.warn("Settings file does not exist, creating one...");
            createDefault();
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(Settings.PROPERTIES_FILE);
        configFile.read(fileInputStream);
        fileInputStream.close();
    }

    private synchronized void createDefault() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(Settings.PROPERTIES_FILE);
        configFile.clear();
        configFile.put("owner", "", "The nickname of the owner for this bot.");
        configFile.put("nick", "Pircbot", "The nickname for this bot.");
        configFile.put("name", "Pircbot", "The name for this bot.");
        configFile.put("password", "", "The password for this bot (uses NickServ).");
        configFile.put("command-flag", "!", "Flag to signal if the message is a command. Ex: \"!chat Hello world!\"");
        configFile.write(fileOutputStream, "Settings for Slave-Bot");
        fileOutputStream.close();
    }

    public synchronized String getOwner() {
        return (String)configFile.get("owner");
    }

    public synchronized String getNick() {
        return (String)configFile.get("nick");
    }

    public synchronized String getName() {
        return (String)configFile.get("name");
    }

    public synchronized String getPassword() {
        return (String)configFile.get("password");
    }

    public synchronized String getCommandFlag() {
        return (String)configFile.get("command-flag");
    }

}
