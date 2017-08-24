package org.slave.slavebot;

import org.slave.lib.api.CommentProperties;
import org.slave.lib.helpers.ConfigHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Master801 on 11/29/2015 at 12:26 PM.
 *
 * @author Master801
 */
public final class Settings {

    private static final File PROPERTIES_FILE = new File("settings.properties");

    private static final String CONFIG_KEY_OWNER = "owner";
    private static final String CONFIG_KEY_NICK = "nick";
    private static final String CONFIG_KEY_NAME = "name";
    private static final String CONFIG_KEY_PASSWORD = "password";
    private static final String CONFIG_KEY_COMMAND_FLAG = "command-flag";

    public static final Settings INSTANCE = new Settings();

    private final Object lock = new Object();
    private final CommentProperties configFile = ConfigHelper.createCommentProperties();

    public void load() throws IOException {
        synchronized(lock) {
            if (!Settings.PROPERTIES_FILE.exists()) {
                SlaveBot.SLAVE_BOT_LOGGER.warn("Settings file does not exist, creating one...");
                createDefault();
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(Settings.PROPERTIES_FILE);
            configFile.read(fileInputStream);
            fileInputStream.close();
        }
    }

    private void createDefault() throws IOException {
        synchronized(lock) {
            FileOutputStream fileOutputStream = new FileOutputStream(Settings.PROPERTIES_FILE);
            configFile.put(CONFIG_KEY_OWNER, "", "The nickname of the owner for this bot");
            configFile.put(CONFIG_KEY_NICK, "Pircbot", "The nickname for this bot");
            configFile.put(CONFIG_KEY_NAME, "Pircbot", "The name for this bot");
            configFile.put(CONFIG_KEY_PASSWORD, "", "The password for this bot (uses NickServ)");
            configFile.put(CONFIG_KEY_COMMAND_FLAG, "!", "Flag to signal if the message is a command Ex: \"!chat Hello world!\"");
            configFile.write(fileOutputStream, "Settings for Slave-Bot");
            fileOutputStream.close();
        }
    }

    public String getOwner() {
        synchronized(lock) {
            if (!configFile.hasKey(CONFIG_KEY_OWNER)) throw new NullPointerException("Config does not have an owner set!");
            return (String)configFile.get(CONFIG_KEY_OWNER);
        }
    }

    public String getNick() {
        synchronized(lock) {
            if (!configFile.hasKey(CONFIG_KEY_NICK)) throw new NullPointerException("Config does not have a nick set!");
            return (String)configFile.get(CONFIG_KEY_NICK);
        }
    }

    public String getName() {
        synchronized(lock) {
            if (!configFile.hasKey(CONFIG_KEY_NAME)) throw new NullPointerException("Config does not have a name set!");
            return (String)configFile.get(CONFIG_KEY_NAME);
        }
    }

    public String getPassword() {
        synchronized(lock) {
            if (!configFile.hasKey(CONFIG_KEY_PASSWORD)) throw new NullPointerException("Config does not have a password set!");
            return (String)configFile.get(CONFIG_KEY_PASSWORD);
        }
    }

    public String getCommandFlag() {
        if (!configFile.hasKey(CONFIG_KEY_COMMAND_FLAG)) throw new NullPointerException("Config does not have a command flag set!");
        synchronized(lock) {
            return (String)configFile.get(CONFIG_KEY_COMMAND_FLAG);
        }
    }

}
