package org.slave.slavebot;

/**
 * Created by Master801 on 11/29/2015 at 7:13 AM.
 *
 * @author Master801
 */
public final class Main {

    public static void main(final String[] arguments) {
        SlaveBot slaveBot = new SlaveBot();
        try {
            slaveBot.init();
        } catch(Exception e) {
            SlaveBot.SLAVE_BOT_LOGGER.error(
                "Failed to initialize!",
                e
            );
        }
    }

}
