package org.slave.slavebot.resources;

import org.slave.lib.api.Copyable;

import java.io.Serializable;

/**
 * Created by Master801 on 11/29/2015 at 11:44 AM.
 *
 * @author Master801
 */
public final class Channel implements Serializable, Copyable<Channel> {

    private final String name, password;

    Channel(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Channel copy() {
        return new Channel(name, password);
    }

}
