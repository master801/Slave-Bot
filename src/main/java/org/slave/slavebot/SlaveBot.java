package org.slave.slavebot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slave.slavebot.resources.Channel;
import org.slave.slavebot.resources.Server;
import org.slave.slavebot.resources.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Master801 on 11/29/2015 at 7:14 AM.
 *
 * @author Master801
 */
public final class SlaveBot {

    public static final Logger SLAVE_BOT_LOGGER = LoggerFactory.getLogger("Slave-Bot");

    private Gson gson;
    private ServerHandler serverHandler;

    SlaveBot() {
        final Object _INTERNAL_USAGE_ONLY = null;
    }

    public void init() throws Exception {
        serverHandler = new ServerHandler(getGson());
        serverHandler.init();
    }

    public Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();

            //<editor-fold desc="Adapters">
            gsonBuilder.registerTypeAdapter(
                Server.class,
                new Server.ServerJson.ServerJsonSerializer()
            );
            gsonBuilder.registerTypeAdapter(
                Server.class,
                new Server.ServerJson.ServerJsonDeserializer()
            );

            gsonBuilder.registerTypeAdapter(
                Channel.class,
                new Channel.ChannelJson.ChannelJsonSerializer()
            );
            gsonBuilder.registerTypeAdapter(
                Channel.class,
                new Channel.ChannelJson.ChannelJsonDeserializer()
            );
            //</editor-fold>


            gson = gsonBuilder.create();
        }
        return gson;
    }

}
