package org.slave.slavebot.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slave.slavebot.SlaveBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Master801 on 11/29/2015 at 11:39 AM.
 *
 * @author Master801
 */
public final class ServerHandler {

    public static final ServerHandler INSTANCE = new ServerHandler();

    private static final File SERVER_JSON_FILE = new File("servers.json");
    private final Object lock = new Object();

    private Gson gson;
    private Server server;

    private ServerHandler() {
    }

    @SuppressWarnings("unchecked")
    public void load() throws IOException {
        synchronized(lock) {
            if (!ServerHandler.SERVER_JSON_FILE.exists()) {
                SlaveBot.SLAVE_BOT_LOGGER.warn("Found no \"{}\" file. Creating a dummy one...", ServerHandler.SERVER_JSON_FILE.getName());
                SlaveBot.SLAVE_BOT_LOGGER.info("Please edit the file to suit your needs.");
                createDefault();
                System.exit(1);
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(SERVER_JSON_FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            JsonReader jsonReader = getGson().newJsonReader(inputStreamReader);

            server = getGson().fromJson(jsonReader, Server.class);

            jsonReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        }
    }

    @SuppressWarnings("unchecked")
    private void createDefault() throws IOException {
        synchronized(lock) {
            Channel channel1 = new Channel("#dummy_channel_1", "");
            Channel channel2 = new Channel("#dummy_channel_2", "");

            Server server = new Server("", 0, "");
            server.addChannel(channel1);
            server.addChannel(channel2);

            FileOutputStream fileOutputStream = new FileOutputStream(ServerHandler.SERVER_JSON_FILE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            JsonWriter jsonWriter = getGson().newJsonWriter(outputStreamWriter);

            getGson().toJson(
                server,
                Server.class,
                jsonWriter
            );

            jsonWriter.flush();
            jsonWriter.close();
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    private Gson getGson() {
        synchronized(lock)  {
            if (gson == null) {
                GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();

                gsonBuilder.registerTypeAdapter(
                    Server.class,
                    Server.ServerJson.ServerJsonSerializer.class
                );
                gsonBuilder.registerTypeAdapter(
                    Server.class,
                    Server.ServerJson.ServerJsonDeserializer.class
                );

                gsonBuilder.registerTypeAdapter(
                    Channel.class,
                    Channel.ChannelJson.ChannelJsonSerializer.class
                );
                gsonBuilder.registerTypeAdapter(
                    Channel.class,
                    Channel.ChannelJson.ChannelJsonDeserializer.class
                );

                gson = gsonBuilder.create();
            }
            return gson;
        }
    }

    public Server getServer() {
        synchronized(lock) {
            return server;
        }
    }

}
