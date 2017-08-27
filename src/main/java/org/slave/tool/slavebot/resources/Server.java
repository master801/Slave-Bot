package org.slave.tool.slavebot.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.IterableHelper;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Master801 on 11/29/2015 at 11:44 AM.
 *
 * @author Master801
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class Server {

    @NonNull
    @Getter
    private final String name, password;

    @Getter
    private final int port;

    private List<Channel> channels;

    private Server(final String name, final String password, final int port, final List<Channel> channels) {//internal constructor for deserializer
        this(name, password, port);
        this.channels = ImmutableList.copyOf(channels);
    }

    public void addChannel(final Channel channel) {
        if (channel == null) return;
        if (channels == null) channels = Lists.newArrayList();
        if (!hasChannel(channel)) channels.add(channel);
    }

    public boolean removeChannel(final Channel channel) {
        if (channel == null || IterableHelper.isNullOrEmpty(channels)) return false;
        if (hasChannel(channel)) return channels.remove(channel);
        return false;
    }

    public boolean hasChannel(final Channel channel) {
        if (channel == null || IterableHelper.isNullOrEmpty(channels)) return false;
        return channels.contains(channel);
    }

    public ImmutableList<Channel> getChannels() {
        return ImmutableList.copyOf(channels);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ServerJson {

        static final String PROPERTY_NAME = "name";
        static final String PROPERTY_PORT = "port";
        static final String PROPERTY_PASSWORD = "password";
        static final String PROPERTY_CHANNELS = "channels";

        public static final class ServerJsonSerializer implements JsonSerializer<Server> {

            @Override
            public JsonElement serialize(final Server src, final Type typeOfSrc, final JsonSerializationContext context) {
                JsonObject jsonServer = new JsonObject();

                JsonPrimitive jsonName = new JsonPrimitive(src.getName());
                JsonPrimitive jsonPort = new JsonPrimitive(src.getPort());
                JsonPrimitive jsonPassword = new JsonPrimitive(src.getPassword());
                JsonArray jsonChannels = new JsonArray();

                if (!IterableHelper.isNullOrEmpty(src.channels)) {
                    for(Channel channel : src.channels) {
                        jsonChannels.add(
                            context.serialize(channel)
                        );
                    }
                }

                jsonServer.add(
                    ServerJson.PROPERTY_NAME,
                    jsonName
                );
                jsonServer.add(
                    ServerJson.PROPERTY_PORT,
                    jsonPort
                );
                jsonServer.add(
                    ServerJson.PROPERTY_PASSWORD,
                    jsonPassword
                );
                jsonServer.add(
                    ServerJson.PROPERTY_CHANNELS,
                    jsonChannels
                );

                return jsonServer;
            }

        }

        public static final class ServerJsonDeserializer implements JsonDeserializer<Server> {

            @Override
            public Server deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonServer = json.getAsJsonObject();

                JsonPrimitive jsonName = jsonServer.get(ServerJson.PROPERTY_NAME).getAsJsonPrimitive();
                JsonPrimitive jsonPort = jsonServer.get(ServerJson.PROPERTY_PORT).getAsJsonPrimitive();
                JsonPrimitive jsonPassword = jsonServer.get(ServerJson.PROPERTY_PASSWORD).getAsJsonPrimitive();
                JsonArray jsonChannels = jsonServer.get(ServerJson.PROPERTY_CHANNELS).getAsJsonArray();

                if (jsonName != null && jsonPort != null && jsonPassword != null) {
                    String name = jsonName.getAsString();
                    int port = jsonPort.getAsInt();
                    String password = jsonPassword.getAsString();
                    List<Channel> channels = null;

                    if (jsonChannels != null && jsonChannels.size() > 0) {
                        channels = Lists.newArrayList();
                        for(JsonElement jsonChannel : jsonChannels) {
                            Channel channel = context.deserialize(
                                jsonChannel,
                                Channel.class
                            );
                            channels.add(channel);
                        }
                    }

                    return new Server(
                        name,
                        password,
                        port,
                        channels
                    );
                }

                return null;
            }

        }

    }

}
