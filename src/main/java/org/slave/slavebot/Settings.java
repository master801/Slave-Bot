package org.slave.slavebot;

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
            configFile.clear();
            configFile.put("owner", "", "The nickname of the owner for this bot");
            configFile.put("nick", "Pircbot", "The nickname for this bot");
            configFile.put("name", "Pircbot", "The name for this bot");
            configFile.put("password", "", "The password for this bot (uses NickServ)");
            configFile.put("command-flag", "!", "Flag to signal if the message is a command Ex: \"!chat Hello world!\"");
            configFile.write(fileOutputStream, "Settings for Slave-Bot");
            fileOutputStream.close();
        }
    }

    public String getOwner() {
        synchronized(lock) {
            return (String)configFile.get("owner");
        }
    }

    public String getNick() {
        synchronized(lock) {
            return (String)configFile.get("nick");
        }
    }

    public String getName() {
        synchronized(lock) {
            return (String)configFile.get("name");
        }
    }

    public String getPassword() {
        synchronized(lock) {
            return (String)configFile.get("password");
        }
    }

    public String getCommandFlag() {
        synchronized(lock) {
            return (String)configFile.get("command-flag");
        }
    }

}
