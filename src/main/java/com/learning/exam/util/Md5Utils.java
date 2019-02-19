package com.learning.exam.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;

/**
 * @author liuzihao
 * @date 2019-01-31  12:05
 */
public class Md5Utils {
    private static final String salt="$1$rasmusle$rISCgZzpwk3UhDidwXvin0";
    private static final Charset UTF8_CHARSET=Charset.forName("utf-8");
    public  static String md5(String value){
        String saltPwd=saltPwd(value);
        byte[] oneBytes = saltPwd.getBytes(UTF8_CHARSET);
        String oneMd5Result = DigestUtils.md5Hex(oneBytes);
        byte[] twoBytes = oneMd5Result.getBytes(UTF8_CHARSET);
        return DigestUtils.md5Hex(twoBytes);
    }
    private static String saltPwd(String value){
        StringBuffer sb = new StringBuffer();
        return sb.append(salt).append(value).toString();
    }
}
