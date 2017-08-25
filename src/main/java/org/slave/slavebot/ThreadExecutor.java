package org.slave.slavebot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Master801 on 12/29/2015 at 5:56 PM.
 *
 * @author Master801
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class ThreadExecutor {

    private final Object lock = new Object();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

}
