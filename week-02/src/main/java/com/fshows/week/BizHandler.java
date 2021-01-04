/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week;

import com.fshows.week.utils.AsyncExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangjianguan
 * @version BizHandler.java, v 0.1 2021-01-04 5:10 下午 wangjianguan
 */
public class BizHandler {


    /**
     * 构造对象
     *
     * @return
     */
    public static BizHandler build() {
        return new BizHandler();
    }

    /**
     * 处理具体业务
     *
     * @param message
     */
    public void business(String message) {
        // 获取线程池
        ThreadPoolExecutor workerExecutor = AsyncExecutor.workerExecutor;
        workerExecutor.execute(() -> {
            // 异步处理
            if (null != message && !"".equals(message)) {
                System.out.println("BizHandler 客户端发送过来的消息内容为:" + message);
            }
        });
    }
}