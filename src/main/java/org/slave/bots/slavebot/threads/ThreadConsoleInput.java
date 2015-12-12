package org.slave.bots.slavebot.threads;

import org.slave.bots.slavebot.Settings;
import org.slave.bots.slavebot.SlaveBot;
import org.slave.lib.helpers.StringHelper;

import java.io.*;

/**
 * Created by Master801 on 12/9/2015 at 6:01 PM.
 *
 * @author Master801
 */
public final class ThreadConsoleInput extends Thread {

    private static boolean requestedStop = false;

    private final SlaveBot bot;

    private InputStreamReader consoleInputStreamReader = null;
    private BufferedReader consoleBufferedReader = null;

    public ThreadConsoleInput(SlaveBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            if (consoleInputStreamReader == null) consoleInputStreamReader = new InputStreamReader(System.in);
            if (consoleBufferedReader == null) consoleBufferedReader = new BufferedReader(consoleInputStreamReader);
            while(!ThreadConsoleInput.requestedStop) {
                final String line = consoleBufferedReader.readLine();
                if (!StringHelper.isNullOrEmpty(line) && line.startsWith(Settings.INSTANCE.getCommandFlag())) {
                    bot.doCommand(null, null, null, null, line);
                }
            }
            consoleBufferedReader.close();
            consoleInputStreamReader.close();
            consoleBufferedReader = null;
            consoleInputStreamReader = null;
        } catch(IOException e) {
            SlaveBot.SLAVE_BOT_LOGGER.catching(e);
        }
    }

    public static synchronized void stopThread() {
        ThreadConsoleInput.requestedStop = true;
    }

}
