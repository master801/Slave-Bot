package org.slave.slavebot.api;

import org.slave.slavebot.api.exception.CommandException;

/**
 * Created by Master801 on 12/26/2015 at 9:47 AM.
 *
 * @author Master801
 */
public interface Bot {

    /**
     * Fired when a message is sent in the chat.
     *
     * @param channel The channel the message came from
     * @param senderNickName The sender's nick-name
     * @param senderHostName The sender's host-name
     * @param message The sender's message
     */
    void onMessage(final String channel, final String senderNickName, final String senderHostName, final String message);

    /**
     * Fired when a command is used.
     *
     * @param channel The channel the command was sent in
     * @param senderNickName The sender's nick-name
     * @param senderHostName The sender's host-name
     * @param commandName The command's name
     * @param completeLine The complete line of the message without the command's name
     */
    void onCommand(final String channel, final String senderNickName, final String senderHostName, final String commandName, final String completeLine) throws CommandException;

    /**
     * Send a message
     *
     * @param recipient Who is receiving this? Can be either a channel (make sure to have a pound-sign  #  before the name!) or a person's nick-name
     * @param message The message to send
     */
    void sendMessage(final String recipient, final String message);

    /**
     * @param channel The channel to get the users from. The bot <b>MUST</b> be connected to this channel for it to receive data from it.
     *
     * @return The users' nick-name in the specified channel. This will either return null or an empty array if the bot is not connected to the channel.
     */
//    String[] getUsers(final String channel);//TODO

    /**
     * @return The names of the channels the bot is connected to
     */
    String[] getConnectedChannels();

    /**
     * Joins the specified channel
     *
     * @param channel The channel to join. This <b>MUST</b> have a pound-sign ( # ) before the name!
     * @param channelPassword The password for the channel, this is used only if the channel has a password in order for the bot to join. You may pass in null for this parameter if the channel has no password.
     */
    void joinChannel(final String channel, final String channelPassword);

}
