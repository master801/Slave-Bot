package org.slave.slavebot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Master801 on 12/29/2015 at 5:56 PM.
 *
 * @author Master801
 */
public final class ThreadExecutor {

    public static final ThreadExecutor INSTANCE = new ThreadExecutor();

    private final Object lock = new Object();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute(final Runnable runnable) {
        if (runnable == null) return;
        synchronized(lock) {
            new Thread(runnable).start();
        }
    }

}
