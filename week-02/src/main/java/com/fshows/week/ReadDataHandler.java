/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author wangjianguan
 * @version ReadDataHandler.java, v 0.1 2021-01-04 4:42 下午 wangjianguan
 */
public class ReadDataHandler {

    /**
     * 读取数据
     *
     * @param selectionKey
     */
    public void readData(SelectionKey selectionKey) throws Exception {
        // 定义channel
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        String message = new String(buffer.array());
        // 处理业务
        BizHandler.build().business(message);
    }
}