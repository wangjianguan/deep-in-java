/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangjianguan
 * @version ThreadFactoryImpl.java, v 0.1 2021-01-04 2:26 下午 wangjianguan
 */
public class ThreadFactoryImpl implements ThreadFactory {

    private final AtomicLong threadIndex = new AtomicLong(0);

    private final String threadNamePrefix;

    private final boolean daemon;

    public ThreadFactoryImpl(final String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public ThreadFactoryImpl(final String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    /**
     * 构建实例
     *
     * @param threadName
     * @return
     */
    public static ThreadFactoryImpl instance(String threadName) {
        return new ThreadFactoryImpl(threadName);
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadNamePrefix + this.threadIndex.incrementAndGet());
        thread.setDaemon(daemon);
        return thread;
    }
}