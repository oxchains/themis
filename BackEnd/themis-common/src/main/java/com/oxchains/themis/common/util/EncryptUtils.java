<<<<<<< HEAD:BackEnd/themis-common/src/main/java/com/oxchains/common/util/EncryptUtils.java
package com.oxchains.common.util;
=======
package com.oxchains.themis.common.util;
>>>>>>> b54ef991ebf23b343ec4f70ab27edc8e081f0b78:BackEnd/themis-common/src/main/java/com/oxchains/themis/common/util/EncryptUtils.java

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ccl
 * @Time 2017-10-12 15:49
 * @Name EncryptUtil
 * @Desc: 加密工具类
 */
public class EncryptUtils {
    public static String encodeMD5(String str){
        return encrypt(str,"MD5");
    }
    public static String encodeSHA1(String str){
        return encrypt(str,"SHA-1");
    }
    public static String encodeSHA256(String str){
        return encrypt(str,"SHA-256");
    }
    public static String encodeBase64(String str){
        BASE64Encoder encoder=new BASE64Encoder();
        return encoder.encode(str.getBytes());
    }
    public static String decodeBase64(String str){
        BASE64Decoder decoder=new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String encrypt(String src,String type){
        MessageDigest md=null;
        String result=null;
        byte[] b=src.getBytes();
        try {
            md=MessageDigest.getInstance(type);
            md.update(b);

            result=new BigInteger(1,md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
    private EncryptUtils(){}

    public static void main(String[] args) {
        String str="123456";
        String res=null;
        res=EncryptUtils.encodeBase64(str);
        System.out.println(res);
        res=EncryptUtils.encodeMD5(str);
        System.out.println(res);
        res=EncryptUtils.encodeSHA1(str);
        System.out.println(res);
        res=EncryptUtils.encodeSHA256(str);
        System.out.println(res);

    }
}
