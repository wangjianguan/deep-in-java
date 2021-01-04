/**
 * fshows.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.fshows.week;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 主 reactor
 *
 * @author wangjianguan
 * @version MainReactor.java, v 0.1 2021-01-04 2:20 下午 wangjianguan
 */
public class MainReactor {

    /**
     * 端口
     */
    private static final int PORT = 9875;
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 服务端用于监听客户端的channel
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * 定义子reactor
     */
    private SubReactor subReactor;

    /**
     * 构造并初始化一个对象
     */
    public MainReactor() {
        try {
            // 获得selector选择器
            selector = Selector.open();
            // 获得serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 把serverChannel注册到选择器上面
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception ex) {
            System.out.println("MainReactor 构造并初始化MainReactor 异常,程序退出");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // 构造子reactor
        SubReactor subReactor = new SubReactor();

        // 构造主reactor
        MainReactor mainReactor = new MainReactor();
        // 给主reactor设置子reactor
        mainReactor.setSubReactor(subReactor);

        // 启动子reactor中,开始从selector上去监听读事件
        subReactor.listenReadable();
        // 启动监听
        mainReactor.listen();


    }

    public void setSubReactor(SubReactor subReactor) {
        this.subReactor = subReactor;
    }

    /**
     * 监听客户端
     */
    public void listen() {
        try {
            System.out.println("MainReactor 开始监听请求来的客户端,线程名称:" + Thread.currentThread().getName());
            while (true) {
                System.out.println("MainReactor 阻塞等待注册,线程名称:" + Thread.currentThread().getName());
                // 阻塞监听
                int selectCount = selector.select();
                if (selectCount > 0) {
                    System.out.println("MainReactor 阻塞等待注册完成,有请求的客户端了,线程名称:" + Thread.currentThread().getName() + "selectCount :" + selectCount);
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        // 监听accept
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            // 设置非阻塞
                            socketChannel.configureBlocking(false);

                            // 注册到selector
                            subReactor.register(socketChannel);
                        }
                        selectionKeyIterator.remove();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}