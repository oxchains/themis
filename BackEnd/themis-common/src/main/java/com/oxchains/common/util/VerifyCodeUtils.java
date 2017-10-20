package com.oxchains.common.util;

import java.util.Random;

/**
 * @Author ccl
 * @Time 2017-10-19 14:10
 * @Name VerifyCodeUtils
 * @Desc: 验证码生成工具
 */
public class VerifyCodeUtils {
    private VerifyCodeUtils(){}
    public static String getRandCode(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }
    private static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static void main(String[] args) {

    }
}
