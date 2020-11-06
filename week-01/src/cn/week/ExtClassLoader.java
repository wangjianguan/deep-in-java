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
     *
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

    private byte[] loadData(String fullUrl) throws Exception {
        InputStream inputStream = new FileInputStream(fullUrl);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bys)) != -1) {
            bos.write(bys, 0, len);
        }
        byte[] bytes = bos.toByteArray();
        byte[] out = new byte[bytes.length];
        int i = 0;
        for (byte aByte : bytes) {
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