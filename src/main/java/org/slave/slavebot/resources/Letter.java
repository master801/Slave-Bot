package org.slave.slavebot.resources;

import org.slave.lib.api.Copyable;
import org.slave.lib.helpers.ReflectionHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by Master801 on 12/24/2015 at 10:20 AM.
 *
 * @author Master801
 */
public final class Letter implements Serializable, Copyable<Letter> {

    public static final String HEADER = "LETTER_v0";

    private static final long serialVersionUID = 5419232013811985204L;

    private final String uuid;
    private final String sender, recipient;
    private final String message;

    public Letter(final String uuid, final String sender, final String recipient, final String message) {
        this.uuid = uuid;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    /**
     * @return The UUID of the message (this is used for deleting messages)
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * @return The name of the person who sent the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return The name of the person who the message was sent to
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link Serializable}
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeByte(ObjectStreamConstants.TC_STRING);
        out.writeUTF(Letter.HEADER);


        out.writeByte(ObjectStreamConstants.TC_STRING);
        out.writeUTF(uuid);

        out.writeByte(ObjectStreamConstants.TC_STRING);
        out.writeUTF(sender);

        out.writeByte(ObjectStreamConstants.TC_STRING);
        out.writeUTF(recipient);

        out.writeByte(ObjectStreamConstants.TC_STRING);
        out.writeUTF(message);

        out.flush();
        out.close();
    }

    /**
     * {@link Serializable}
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (!in.readUTF().equals(Letter.HEADER)) throw new IOException("Not a valid letter!");
        try {
            ReflectionHelper.setFieldValue(ReflectionHelper.getField(getClass(), "uuid"), this, in.readUTF());
            ReflectionHelper.setFieldValue(ReflectionHelper.getField(getClass(), "sender"), this, in.readUTF());
            ReflectionHelper.setFieldValue(ReflectionHelper.getField(getClass(), "recipient"), this, in.readUTF());
            ReflectionHelper.setFieldValue(ReflectionHelper.getField(getClass(), "message"), this, in.readUTF());
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        in.close();
    }

    /**
     * {@link Serializable}
     */
    private void readObjectNoData() throws ObjectStreamException {
        //NOOP
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    @Override
    public Letter copy() {
        return new Letter(new String(uuid), new String(sender), new String(recipient), new String(message));
    }

}
