package org.slave.slavebot.resources;

import org.slave.lib.api.Copyable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Master801 on 11/29/2015 at 11:44 AM.
 *
 * @author Master801
 */
public final class Server implements Serializable, Copyable<Server> {

    private static final long serialVersionUID = 4064671318681946206L;

    private final String name, password;
    private final int port;
    private final ArrayList<Channel> channels = new ArrayList<>();

    public Server(final String name, final int port, final String password) {
        this.name = name;
        this.port = port;
        this.password = password;
    }

    private Server(final String name, final int port, final String password, final ArrayList<Channel> channels) {
        this.name = name;
        this.port = port;
        this.password = password;

        this.channels.clear();
        this.channels.addAll(channels);
    }

    public Channel addChannel(String name, String password) {
        Channel channel = new Channel(name, password);
        channels.add(channel);
        return channel;
    }

    public void removeChannel(String name) {
        for(Channel channel : channels) {
            if (channel.getName().equals(name)) {
                channels.remove(channel);
                break;
            }
        }
    }

    public Channel[] getChannels() {
        return channels.toArray(new Channel[channels.size()]);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Server copy() {
        return new Server(name, port, password, channels);
    }

}
