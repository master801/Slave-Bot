package org.slave.bots.slavebot;

import java.io.*;
import java.util.Properties;

/**
 * Created by Master801 on 11/29/2015 at 12:26 PM.
 *
 * @author Master801
 */
public final class Settings {

    public static final Settings INSTANCE = new Settings();

    private static final File PROPERTIES_FILE = new File("settings.properties");

    private final Properties properties = new Properties();

    public void load() throws IOException {
        if (!Settings.PROPERTIES_FILE.exists()) {
            SlaveBot.SLAVE_BOT_LOGGER.warn("Settings file does not exist, creating one...");
            createDefault();
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(Settings.PROPERTIES_FILE);
        properties.load(fileInputStream);
        fileInputStream.close();
    }

    private void createDefault() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(Settings.PROPERTIES_FILE);
        properties.clear();
        properties.put("owner", "");
        properties.put("nick", "Pircbot");
        properties.put("name", "Pircbot");
        properties.put("password", "");
        properties.store(fileOutputStream, "Settings for Slave-Bot");
        fileOutputStream.close();
    }

    public String getOwner() {
        return properties.getProperty("owner");
    }

    public String getNick() {
        return properties.getProperty("nick");
    }

    public String getName() {
        return properties.getProperty("name");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

}
