package com.zkc.servlet;


import com.zkc.pojo.Fuser;
import com.zkc.service.FuserService;
import com.zkc.service.impl.FuserServiceImpl;
import com.zkc.utils.AESFileUtil;
import com.zkc.utils.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author zkc
 * @create 2020-04-22 21:03
 */
public class UploadServlet extends HttpServlet {
    private FuserService fuserService = new FuserServiceImpl();
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //验证签名
        String header1 = request.getHeader("X-Signature");
        String publicKeyStr = request.getHeader("publicKeyStr");
        String header3 = request.getHeader("X-SID");
        System.out.println("验证签名X-SID："+header3);
        System.out.println("验证签名publicKeyStr："+publicKeyStr);
        System.out.println("验证签名X-Signature："+header1);
        try {
            PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
            System.out.println("验证成功"+RSAUtil.verify(header3, header1, publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //记录今日日期
        Date date = new Date();
        java.sql.Date today = new java.sql.Date(date.getTime());
        //获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String srcFilename = request.getParameter("filename");//原始文件名
        String srcFilesize = request.getParameter("filesize");//文件大小
        String srcFiletype = request.getParameter("filetype");//文件类型
        String newFilename = UUID.randomUUID().toString().replace("-","");
        System.out.println("我听到了");
        System.out.println(request.getParameter("password"));

        realPath=realPath+"uploads\\";
        //文件起始地址
        String sourcename = realPath+srcFilename;
        //加密后文件存放地址
        String dust = realPath+today+"\\";
        //文件目标地址 若不存在则创建文件夹
        File file = new File(dust);
        if(!file.exists()){
            //创建该文件夹
            file.mkdirs();
        }
        //生成随机AES秘钥
        String strKeyAES = null;
        try {
            strKeyAES = AESFileUtil.getStrKeyAES();
            System.out.println("AES钥匙!!!!："+strKeyAES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //对文件进行加密并存储
        boolean b = com.zkc.utils.AESFileUtil.encryptFile(sourcename, dust+newFilename,strKeyAES);
        System.out.println("是否加密成功："+b);
        //删除原文件
        AESFileUtil.delFile(sourcename);
        try {
            System.out.println("Base64处理后的公钥：" + publicKeyStr);
            // 获得原始的公钥
            PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
            // 公钥加密
            byte[] publicEncryBytes = RSAUtil.publicEncrytype(strKeyAES.getBytes(), publicKey);
            //公钥加密后的字符串(经BASE64处理)：
            System.out.println("公钥加密后的字符串(经BASE64处理 aes秘钥)：" + Base64.getEncoder().encodeToString(publicEncryBytes));
            //把用户信息存入数据库
            fuserService.saveUser(new Fuser(srcFilename,srcFilesize,srcFiletype,new java.sql.Date(new Date().getTime()),dust,Base64.getEncoder().encodeToString(publicEncryBytes),newFilename));
        } catch (Exception e) {
            e.printStackTrace();
        }





        //向HTTP发送方返回响应数据
            response.getWriter().write("uuid:"+newFilename);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }

}
