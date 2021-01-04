/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week;

import com.fshows.week.utils.AsyncExecutor;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * 子 reactor
 *
 * @author wangjianguan
 * @version SubReactor.java, v 0.1 2021-01-04 4:40 下午 wangjianguan
 */
public class SubReactor {

    /**
     * 选择器
     */
    private Selector selector;

    /**
     * 构造并初始化子reactor
     */
    public SubReactor() {
        try {
            selector = SelectorProvider.provider().openSelector();
        } catch (Exception ex) {
            System.out.println("初始化子Reactor失败,程序退出");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * 将客户端注册到选择器
     *
     * @param socketChannel
     */
    public void register(SocketChannel socketChannel) {
        try {
            // 将socketChannel注册到选择器,注册一个读取的时间
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception ex) {
            System.out.println("SubReactor 注册channel到选择器失败");
            ex.printStackTrace();
        }
    }


    /**
     * 监听时间,处理可读的事件
     */
    public void listenReadable() {
        AsyncExecutor.buildPool("listen_readable_").execute(() -> {
            System.out.println("SubReactor 异步处理,想要读取数据 >> listenReadable,当前线程名称:" + Thread.currentThread().getName());
            while (true) {
                try {
                    selector.select(2000);
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        try {
                            // 分发处理
                            this.dispatch(selectionKey);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("处理异常");
                            if (null != selectionKey) {
                                // 取消注册
                                selectionKey.cancel();
                                SelectableChannel channel = selectionKey.channel();
                                if (null != channel) {
                                    // 关闭通道
                                    channel.close();
                                }


                            }
                        }
                        // TODO: 2021/1/4 像channel设置响应

                        // 删除key
                        selectionKeyIterator.remove();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        });

    }

    /**
     * 分发处理
     *
     * @param selectionKey
     */
    private void dispatch(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isReadable()) {
            // 异步读取数据
            new ReadDataHandler().readData(selectionKey);
        }
        // TODO: 2021/1/4 当监听了其他操作后需要在此处增加相应实现
    }


}