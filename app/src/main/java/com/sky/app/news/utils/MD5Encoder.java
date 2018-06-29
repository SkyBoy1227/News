package com.sky.app.news.utils;

import java.security.MessageDigest;

/**
 * Created with Android Studio.
 * 描述: MD5工具类
 * Date: 2018/6/29
 * Time: 14:33
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MD5Encoder {

    public static String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
