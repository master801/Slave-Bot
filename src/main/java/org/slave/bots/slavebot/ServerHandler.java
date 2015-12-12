package org.slave.bots.slavebot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slave.bots.slavebot.resources.Server;

import java.io.*;

/**
 * Created by Master801 on 11/29/2015 at 11:39 AM.
 *
 * @author Master801
 */
public final class ServerHandler {

    public static final ServerHandler INSTANCE = new ServerHandler();

    private static final File SERVER_JSON_FILE = new File("servers.json");

    private Server server = null;

    private ServerHandler() {
    }

    @SuppressWarnings("unchecked")
    public void load() throws IOException {
        if (!ServerHandler.SERVER_JSON_FILE.exists()) {
            SlaveBot.SLAVE_BOT_LOGGER.warn("Found no \"{}\" file. Creating a dummy one...", ServerHandler.SERVER_JSON_FILE.getName());
            SlaveBot.SLAVE_BOT_LOGGER.info("Please edit the file to suit your needs.");
            createDefault();
            System.exit(1);
            return;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject serverJSON;

        FileInputStream fileInputStream = new FileInputStream(ServerHandler.SERVER_JSON_FILE);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        try {
            serverJSON = (JSONObject)jsonParser.parse(inputStreamReader);
        } catch(ParseException e) {
            SlaveBot.SLAVE_BOT_LOGGER.error("Couldn't parse server JSON file!");
            return;
        }

        if (serverJSON != null) {
            server = new Server((String)serverJSON.get("name"), (int) ((long)serverJSON.get("port")), (String)serverJSON.get("password"));

            JSONArray channels = (JSONArray)serverJSON.get("channels");
            for(Object channelObject : channels) {
                JSONObject channel = (JSONObject)channelObject;

                String channelName = (String)channel.get("name");
                String channelPassword = (String)channel.get("password");
                if (!channelName.startsWith("#")) channelName = "#" + channelName;
                server.addChannel(channelName, channelPassword);
            }
        }

        inputStreamReader.close();
        fileInputStream.close();
    }

    @SuppressWarnings("unchecked")
    private void createDefault() throws IOException {
        JSONObject serverJSON = new JSONObject();
        serverJSON.put("name", "");
        serverJSON.put("port", 0);
        serverJSON.put("password", "");

        JSONArray channels = new JSONArray();

        JSONObject dummyChannel1 = new JSONObject();
        dummyChannel1.put("name", "#dummy_channel_1");
        dummyChannel1.put("password", "");
        channels.add(dummyChannel1);

        JSONObject dummyChannel2 = new JSONObject();
        dummyChannel2.put("name", "#dummy_channel_2");
        dummyChannel2.put("password", "");
        channels.add(dummyChannel2);

        serverJSON.put("channels", channels);

        FileWriter fileWriter = new FileWriter(ServerHandler.SERVER_JSON_FILE);
        serverJSON.writeJSONString(fileWriter);
        fileWriter.close();
    }

    public Server getServer() {
        return server;
    }

}
