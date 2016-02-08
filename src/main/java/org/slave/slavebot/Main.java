package org.slave.slavebot;

import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.slavebot.resources.Channel;
import org.slave.slavebot.resources.ServerHandler;

import java.io.IOException;

/**
 * Created by Master801 on 11/29/2015 at 7:13 AM.
 *
 * @author Master801
 */
public final class Main {

    public static final String VERSION = "@VERSION@";

    public static void main(final String[] arguments) {
        try {
            Settings.INSTANCE.load();//Initialize settings before the bot
            ServerHandler.INSTANCE.load();
        } catch(IOException e) {
            SlaveBot.SLAVE_BOT_LOGGER.error("Caught an exception while initializing!", e);
            System.exit(-1);
            return;
        }

        SlaveBot slaveBot = new SlaveBot();
        try {
            final String serverName = ServerHandler.INSTANCE.getServer().getName();
            final int serverPort = ServerHandler.INSTANCE.getServer().getPort();
            final String serverPassword = ServerHandler.INSTANCE.getServer().getPassword();

            slaveBot.connect(serverName, serverPort, serverPassword);
        } catch(Exception e) {
            SlaveBot.SLAVE_BOT_LOGGER.error("Caught an exception while connecting!", e);
            System.exit(-1);
            return;
        }

        final String password = Settings.INSTANCE.getPassword();
        if (!StringHelper.isNullOrEmpty(password)) {
            slaveBot.sendMessage("NickServ", "identify " + password);
        } else {
            SlaveBot.SLAVE_BOT_LOGGER.warn("No password was found!");
        }

        final Channel[] channels = ServerHandler.INSTANCE.getServer().getChannels();
        if (!ArrayHelper.isNullOrEmpty(channels)) {
            for(Channel channel : channels) {
                if (!StringHelper.isNullOrEmpty(channel.getPassword())) {
                    slaveBot.joinChannel(!channel.getName().startsWith("#") ? "#" + channel.getName() : channel.getName(), channel.getPassword());
                } else {
                    slaveBot.joinChannel(!channel.getName().startsWith("#") ? "#" + channel.getName() : channel.getName(), null);
                }
            }
        } else {
            SlaveBot.SLAVE_BOT_LOGGER.warn("Found no channels to connect to!");
        }
    }

}
