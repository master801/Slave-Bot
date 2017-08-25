package org.slave.slavebot.resources;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slave.lib.api.Copyable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by Master801 on 11/29/2015 at 11:44 AM.
 *
 * @author Master801
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class Channel implements Serializable, Copyable<Channel> {

    private static final long serialVersionUID = -8996446332888283100L;

    @Getter
    private final String name, password;

    @Override
    public Channel copy() {
        return new Channel(name, password);//Strings are immutable, no need to directly copy them
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Channel) {
            Channel channel = (Channel)obj;
            return channel.getName().equals(name) && channel.getPassword().equals(getPassword());
        }
        return false;
    }

    public static final class ChannelJson {

        static final String PROPERTY_NAME = "name";
        static final String PROPERTY_PASSWORD = "password";

        private ChannelJson() {
            final Object _INTERNAL_USAGE_ONLY = null;
        }

        public static final class ChannelJsonSerializer implements JsonSerializer<Channel> {

            @Override
            public JsonElement serialize(final Channel src, final Type typeOfSrc, final JsonSerializationContext context) {
                JsonObject jsonChannel = new JsonObject();
                jsonChannel.addProperty(
                    ChannelJson.PROPERTY_NAME,
                    src.getName()
                );
                jsonChannel.addProperty(
                    ChannelJson.PROPERTY_PASSWORD,
                    src.getPassword()
                );
                return jsonChannel;
            }

        }

        public static final class ChannelJsonDeserializer implements JsonDeserializer<Channel> {

            @Override
            public Channel deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonChannel = json.getAsJsonObject();

                JsonPrimitive jsonName = jsonChannel.get(ChannelJson.PROPERTY_NAME).getAsJsonPrimitive();
                JsonPrimitive jsonPassword = jsonChannel.get(ChannelJson.PROPERTY_PASSWORD).getAsJsonPrimitive();

                return new Channel(
                    jsonName.getAsString(),
                    jsonPassword.getAsString()
                );
            }

        }

    }

}
