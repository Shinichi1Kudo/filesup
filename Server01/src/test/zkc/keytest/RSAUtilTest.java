package com.zkc.keytest;

/**
 * @author zkc
 * @create 2020-04-23 4:19
 */
import com.zkc.utils.RSAUtil;

import java.security.*;
import java.util.Base64;

/**
 * 对 RSAUtil 进行测试
 */
public class RSAUtilTest {

    public static void main(String[] args) {
        String content = "t17f0d1W5QOFCEk69ynXMQ==";   // 明文内容
        System.out.println("原始字符串是：" + content);
        try {
            // 获得密钥对
            KeyPair keyPair = RSAUtil.getKeyPair();
            // 获得进行Base64 加密后的公钥和私钥 String
            String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
            String publicKeyStr = RSAUtil.getPublicKey(keyPair);
            System.out.println("Base64处理后的私钥：" + privateKeyStr + "\n"
                    + "Base64处理后的公钥：" + publicKeyStr);

            // 获得原始的公钥和私钥，并以字符串形式打印出来
            PrivateKey privateKey = RSAUtil.string2Privatekey(privateKeyStr);
            PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);

            // 公钥加密/私钥解密
            byte[] publicEncryBytes = RSAUtil.publicEncrytype(content.getBytes(), publicKey);
            System.out.println("公钥加密后的字符串(经BASE64处理)：" + Base64.getEncoder().encodeToString(publicEncryBytes));
            byte[] privateDecryBytes = RSAUtil.privateDecrypt(publicEncryBytes, privateKey);
            System.out.println("私钥解密后的原始字符串：" + new String(privateDecryBytes));
            String sign = RSAUtil.sign(content, privateKey);
            System.out.println("签名后的字符串"+sign);
            System.out.println("验证签名后："+RSAUtil.verify(content,sign,publicKey));


            String privateDecryStr = new String(privateDecryBytes, "utf-8");
            if (content.equals(privateDecryStr)) {
                System.out.println("测试通过！");
            } else {
                System.out.println("测试未通过！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}