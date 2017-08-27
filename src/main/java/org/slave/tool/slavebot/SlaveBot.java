package org.slave.tool.slavebot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.tool.slavebot.resources.Channel;
import org.slave.tool.slavebot.resources.Server;
import org.slave.tool.slavebot.resources.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Master801 on 11/29/2015 at 7:14 AM.
 *
 * @author Master801
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class SlaveBot {

    public static final Logger SLAVE_BOT_LOGGER = LoggerFactory.getLogger("Slave-Bot");

    private Gson gson;
    private ThreadExecutor threadExecutor;
    private ServerHandler serverHandler;

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

    public ThreadExecutor getThreadExecutor() {
        if (threadExecutor == null) threadExecutor = new ThreadExecutor();
        return threadExecutor;
    }

}
