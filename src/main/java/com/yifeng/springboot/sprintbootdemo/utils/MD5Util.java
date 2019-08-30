package com.yifeng.springboot.sprintbootdemo.utils;

import java.security.MessageDigest;

/**
 * MD: Message-Digest Algorithm 产生16字节128位的hash value
 * @author yifeng
 *
 */

public class MD5Util {

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 对二进制字符串的每一个二进制字符通过调用 byteToHexString 进行十六进制转换
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte b[]) {

        //通过StringBuffer生成的对象为null
        StringBuffer resultSb = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {

        int n = b;

        if (n < 0) {
            n += 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * charset指的是字符集，比如：UTF-8
     */
    public static String MD5Encode(String origin, String charsetname) {

        String resultString = null;

        try {
            resultString = new String(origin);

            //指定加密算法 MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            if (charsetname == null || "".equals(charsetname)) {

                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                //将需要加密的字符串转换成byte类型的数组，然后进行随机哈希生成byte array（二进制数组）
                // 随后通过byteArrayToHexString转换成hexadecimal（16进制字符串）
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

}
