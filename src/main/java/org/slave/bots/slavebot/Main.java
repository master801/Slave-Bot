package org.slave.bots.slavebot;

import org.jibble.pircbot.IrcException;
import org.slave.bots.slavebot.resources.Channel;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;

import java.io.IOException;

/**
 * Created by Master801 on 11/29/2015 at 7:13 AM.
 *
 * @author Master801
 */
public final class Main {

    public static final String VERSION = "1.0.0";

    public static void main(final String[] arguments) {
        try {
            Settings.INSTANCE.load();//Initialize settings before the bot
            ServerHandler.INSTANCE.load();
        } catch(IOException e) {
            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
            System.exit(-1);
            return;
        }

        final SlaveBot slaveBot = new SlaveBot();
        try {
            slaveBot.connect(ServerHandler.INSTANCE.getServer().getName(), ServerHandler.INSTANCE.getServer().getPort(), !StringHelper.isNullOrEmpty(ServerHandler.INSTANCE.getServer().getPassword()) ? ServerHandler.INSTANCE.getServer().getPassword() : null);
        } catch(IOException | IrcException e) {
            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
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
                    slaveBot.joinChannel(!channel.getName().startsWith("#") ? "#" + channel.getName() : channel.getName());
                }
            }
        } else {
            SlaveBot.SLAVE_BOT_LOGGER.warn("Found no channels to connect to!");
        }
    }

}
