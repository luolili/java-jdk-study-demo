package base.md5;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Demo {
    public static void main(String[] args) {

    }

    public String encodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //-1 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //-2 创建encoder
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //-3 加密
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }
}
