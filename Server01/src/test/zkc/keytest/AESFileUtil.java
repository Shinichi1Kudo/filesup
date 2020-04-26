package com.zkc.keytest;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * @author ZSL
 * @since 2016年12月7日上午10:18:27
 * @desc [文件加密]
 */
public class AESFileUtil {

    private  String key = "";

//
//    static {
//        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        Random random=new Random();
//        StringBuffer sb=new StringBuffer();
//        for(int i=0;i<8;i++){
//            int number=random.nextInt(62);
//            sb.append(str.charAt(number));
//        }
//        key = sb.toString();
//    }

    /**
     * init AES Cipher
     *
     * @param passsword
     * @param cipherMode
     * @return
     */
    public static Cipher initAESCipher(String passsword, int cipherMode) {
        Cipher cipher = null;
        try {
            SecretKey key = getKey(passsword);
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return cipher;
    }

    private static SecretKey getKey(String password) {
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        SecretKeySpec key = null;
        try {
            Arrays.fill(keyBytes, (byte) 0x0);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] passwordBytes = password.getBytes("UTF-8");
            int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
            System.arraycopy(passwordBytes, 0, keyBytes, 0, length);

            key = new SecretKeySpec(keyBytes, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
    /**
     * AES 加密
     *
     * @param encryptPath
     * @param decryptPath
     * @param sKey
     * @return
     */
    public static boolean encryptFile(String encryptPath, String decryptPath, String sKey) {
        File encryptFile = null;
        File decryptfile = null;
        CipherOutputStream cipherOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            encryptFile = new File(encryptPath);
            if (!encryptFile.exists()) {
                throw new NullPointerException("Encrypt file is empty");
            }
            decryptfile = new File(decryptPath);
            if (decryptfile.exists()) {
                decryptfile.delete();
            }
            decryptfile.createNewFile();

            Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
            cipherOutputStream = new CipherOutputStream(new FileOutputStream(decryptfile), cipher);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(encryptFile));

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ((bufferLength = bufferedInputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bufferLength);
            }
            bufferedInputStream.close();
            cipherOutputStream.close();
//            delFile(encryptPath);
        } catch (IOException e) {
            delFile(decryptfile.getAbsolutePath());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }

    /**
     * AES 解密
     *
     * @param encryptPath
     * @param decryptPath
     * @param mKey
     * @return
     */

    public static boolean decryptFile(String encryptPath, String decryptPath, String mKey) {
        File encryptFile = null;
        File decryptFile = null;
        BufferedOutputStream outputStream = null;
        CipherInputStream inputStream = null;
        try {
            encryptFile = new File(encryptPath);
            if (!encryptFile.exists()) {
                throw new NullPointerException("Decrypt file is empty");
            }
            decryptFile = new File(decryptPath);
            if (decryptFile.exists()) {
                decryptFile.delete();
            }
            decryptFile.createNewFile();

            Cipher cipher = initAESCipher(mKey, Cipher.DECRYPT_MODE);

            outputStream = new BufferedOutputStream(new FileOutputStream(decryptFile));
            inputStream = new CipherInputStream(new FileInputStream(encryptFile), cipher);

            int bufferLength;
            byte[] buffer = new byte[1024];

            while ((bufferLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bufferLength);
            }
            inputStream.close();
            outputStream.close();
//            delFile(encryptPath);
        } catch (IOException e) {
            delFile(decryptFile.getAbsolutePath());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }


    /**
     * delete File
     *
     * @param pathFile
     * @return
     */
    public static boolean delFile(String pathFile) {
        boolean flag = false;
        if (pathFile == null && pathFile.length() <= 0) {
            throw new NullPointerException("文件不能为空");
        } else {
            File file = new File(pathFile);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
                flag = true;
            }
        }
        return flag;
    }



    public static String getStrKeyAES() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes("utf-8"));
        keyGen.init(128, secureRandom);   // 这里可以是 128、192、256、越大越安全
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        boolean flag = com.zkc.keytest.AESFileUtil.encryptFile
//                ("E:/pdf/html/2.JPG", "E:/pdf/html/加密后.JPG", key);
//        System.out.println(flag);
//        boolean  flag = com.zkc.keytest.AESFileUtil.decryptFile
//                ( "E:/pdf/html/加密后.txt","E:/pdf/html/解密后.txt", key);

        //进行加密
//        String path="D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\6e7085dfa5a04579b5dbd0bcf241d57f.jpg";
//
//        String dustpath2="D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\6e7085dfa5a04579b5dbd0bcf241d57f.jpg";
//        boolean b = com.zkc.utils.com.zkc.keytest.AESFileUtil.encryptFile(path, dustpath2, com.zkc.utils.com.zkc.keytest.AESFileUtil.key);
//        System.out.println("是否加密成功："+b);
//        String s1 = "D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\692ab513baba4366b9adec6a2353e076_water.jpg";
//        String s2 = "D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\6666.JPG";
//        File source = new File(s1);
//        File dest = new File(s2);
//        source.renameTo(dest);

//        System.out.println(flag);
        String s1 = "D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\6666.JPG";
        String s2 = "D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\3333.JPG";
        String s3 = "D:\\JavaSenior\\JavaWeb\\Server\\target\\Server-1.0-SNAPSHOT\\uploads\\0000.JPG";
        String strKeyAES = getStrKeyAES();
        System.out.println(strKeyAES);
        boolean b = encryptFile(s1, s2, strKeyAES);
        boolean b1 = decryptFile(s2, s3, strKeyAES);
        System.out.println(b);
        System.out.println(b1);



    }
}