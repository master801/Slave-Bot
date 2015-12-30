package org.slave.slavebot;

/**
 * Created by Master801 on 12/29/2015 at 5:56 PM.
 *
 * @author Master801
 */
public final class ThreadExecutor {

    public static final ThreadExecutor INSTANCE = new ThreadExecutor();

    private final Object lock = new Object();

    public void execute(final Runnable runnable) {
        if (runnable == null) return;
        synchronized(lock) {
            new Thread(runnable).start();
        }
    }

}
