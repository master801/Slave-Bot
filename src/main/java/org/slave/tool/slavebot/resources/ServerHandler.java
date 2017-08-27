package org.slave.tool.slavebot.resources;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.FileHelper;
import org.slave.tool.slavebot.SlaveBot;

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
@RequiredArgsConstructor
public final class ServerHandler {

    private final Object lock = new Object();

    @NonNull
    @Getter
    private final Gson gson;

    private final File file = new File(
        FileHelper.getCurrentDirectory(),
        "servers.json"
    );

    private Server server;

    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        synchronized(lock) {
            initSettings();
        }
    }

    private void initSettings() throws IOException {
        if (!file.exists()) {
            SlaveBot.SLAVE_BOT_LOGGER.warn(
                "Found no \"{}\" file. Creating a dummy one...",
                file.getName()
            );
            SlaveBot.SLAVE_BOT_LOGGER.info(
                "Please edit the file to suit your needs."
            );
            createDefault();
            System.exit(1);
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        JsonReader jsonReader = gson.newJsonReader(inputStreamReader);

        server = gson.fromJson(
            jsonReader,
            Server.class
        );

        jsonReader.close();
        inputStreamReader.close();
        fileInputStream.close();
    }

    @SuppressWarnings("unchecked")
    private void createDefault() throws IOException {
        Channel channel1 = new Channel("#dummy_channel_1", "");
        Channel channel2 = new Channel("#dummy_channel_2", "");

        Server server = new Server("", "", 0);
        server.addChannel(channel1);
        server.addChannel(channel2);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        JsonWriter jsonWriter = gson.newJsonWriter(outputStreamWriter);

        gson.toJson(
            server,
            Server.class,
            jsonWriter
        );

        jsonWriter.flush();
        jsonWriter.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public Server getServer() {
        synchronized(lock) {
            return server;
        }
    }

}
