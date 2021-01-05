/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjianguan
 * @version AsyncExecutor.java, v 0.1 2021-01-04 2:21 下午 wangjianguan
 */
public class AsyncExecutor {


    /**
     * worker业务线程池
     */
    public static ThreadPoolExecutor workerExecutor;

    /**
     * 轮询accept线程池
     */
    public static ThreadPoolExecutor acceptExecutor;

    static {
        workerExecutor = new ThreadPoolExecutor(10, 20, 20L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                ThreadFactoryImpl.instance("worker_executor_"));


        acceptExecutor = new ThreadPoolExecutor(10, 20, 20L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                ThreadFactoryImpl.instance("accept_executor_"));


    }


    /**
     * 构建一个线程池,只允许有一个线程
     *
     * @param name
     * @return
     */
    public static ThreadPoolExecutor buildPool(String name) {
        return new ThreadPoolExecutor(1, 1, 20L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1),
                ThreadFactoryImpl.instance(name));
    }
}