/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package cn.week;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author wangjianguan
 * @version ExtClassLoader.java, v 0.1 2020-11-01 9:49 上午 wangjianguan
 */
public class ExtClassLoader extends ClassLoader {


    /**
     * @param dir
     * @param name
     * @param className
     * @return
     * @throws Exception
     */
    public Class<?> loadData(String dir, String name, String className) throws Exception {
        String fullUrl = dir + name;
        byte[] data = loadData(fullUrl);
        return super.defineClass(className, data, 0, data.length);
    }

    /**
     * 读取数据
     *
     * @param fullUrl
     * @return
     * @throws Exception
     */
    private byte[] loadData(String fullUrl) throws Exception {
        InputStream fileInputStream = new FileInputStream(fullUrl);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] outputByte = new byte[1024];
        int len;
        while ((len = fileInputStream.read(outputByte)) != -1) {
            byteArrayOutputStream.write(outputByte, 0, len);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] out = new byte[byteArray.length];
        int i = 0;
        for (byte aByte : byteArray) {
            out[i] = (byte) ((byte) 255 - aByte);
            i++;
        }
        return out;
    }


    public static void main(String[] args) throws Exception {
        ExtClassLoader extClassLoader = new ExtClassLoader();
        Class<?> aClass = extClassLoader.loadData("/Users/wangjg/Downloads/", "Hello.xlass", "Hello");
        Object hello = aClass.newInstance();
        Method hello1 = hello.getClass().getMethod("hello");
        Object invoke = hello1.invoke(hello);
    }
}