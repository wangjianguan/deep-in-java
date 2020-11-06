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
 * 自定义classloader
 *
 * @author wangjianguan
 * @version ExtClassLoader.java, v 0.1 2020-11-01 9:49 上午 wangjianguan
 */
public class ExtClassLoader extends ClassLoader {


    /**
     * 载入数据
     *
     * @param dir
     * @param name
     * @param className
     * @return
     * @throws Exception
     */
    public Class<?> loadData(String dir, String name, String className) throws Exception {
        String fullUrl = dir + name + ".xlass";
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
        byte[] out = null;
        try {
            byte[] outputByte = new byte[1024];
            int len;
            // 读取数据
            while ((len = fileInputStream.read(outputByte)) != -1) {
                byteArrayOutputStream.write(outputByte, 0, len);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            // 解密
            out = new byte[byteArray.length];
            int i = 0;
            for (byte aByte : byteArray) {
                out[i] = (byte) ((byte) 255 - aByte);
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            byteArrayOutputStream.close();
            fileInputStream.close();
        }
        return out;
    }


    public static void main(String[] args) throws Exception {
        ExtClassLoader extClassLoader = new ExtClassLoader();
        Class<?> aClass = extClassLoader.loadData("/Users/wangjg/Downloads/", "Hello", "Hello");
        Object helloObj = aClass.newInstance();
        Method helloMethod = helloObj.getClass().getMethod("hello");
        helloMethod.invoke(helloObj);
    }
}