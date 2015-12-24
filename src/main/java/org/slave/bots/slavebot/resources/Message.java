package org.slave.bots.slavebot.resources;

import org.slave.lib.api.Copyable;

import java.io.Serializable;

/**
 * Created by Master801 on 12/24/2015 at 10:20 AM.
 *
 * @author Master801
 */
public final class Message implements Serializable, Copyable<Message> {

    private final String uuid;
    private final String sender, recipient;
    private final String message;

    public Message(final String uuid, final String sender, final String recipient, final String message) {
        this.uuid = uuid;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getUUID() {
        return uuid;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    @SuppressWarnings("all")
    @Override
    public Message copy() {
        return new Message(new String(uuid), new String(sender), new String(recipient), new String(message));
    }

}
