package com.twosigma.mmia.pe.opsmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by sam on 11/23/15.
 */
public class ExecutorUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorUtils.class);

    private ExecutorUtils() {
        // don't instantiate
    }

    public static ThreadPoolExecutor createSingleThreadDeamonPool(final String threadName, int queueCapacity) {
        final ThreadFactory daemonThreadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName(threadName);
                return thread;
            }
        };
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity), daemonThreadFactory) {
            @Override
            public String toString() {
                return super.toString() + "(thread name = " + threadName + ")";
            }
        };
    }

    public static void logRejectionWarning(RejectedExecutionException e) {
        logger.warn("The limit of pending tasks for the executor is reached. " +
                "This could be due to a unreachable service such as elasticsearch or due to a spike in incoming requests. " +
                "Consider increasing the default capacity limit\n"
                + e.getMessage());
    }
}
