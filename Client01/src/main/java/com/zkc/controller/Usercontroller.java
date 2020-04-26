package com.zkc.controller;

import com.alibaba.fastjson.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.zkc.exception.SysException;
import com.zkc.pojo.Fuser;
import com.zkc.utils.AESFileUtil;
import com.zkc.utils.RSAUtil;
import com.zkc.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.security.*;
import java.sql.Date;
import java.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author zkc
 * @create 2020-04-22 18:48
 */
@Controller
@RequestMapping("/user")
public class Usercontroller {
    public String uuid = "123";
    public String publicKeyStr = "";
    private PrivateKey privateKey;
    String randomString = "";//每次随机生成的字符串

    @RequestMapping("/fileupload")
    public String fileupLoad(HttpServletRequest request, MultipartFile upload, HttpServletResponse response) {
        System.out.println("跨服务器上传");
        HttpURLConnection httpurlconnection = null;
        KeyPair keyPair = null;
        OutputStreamWriter writer = null;
        try {
            //设置请求的编码方式
            request.setCharacterEncoding("utf-8");
            //设置返回时的编码方式
            response.setContentType("text/html;charset=utf-8");
            System.out.println("test1");
            //设置HTTP连接的URL地址，就是第二个应用的URL。如果在同一个计算机上可以将192.168.1.134变成127.0.0.1或者localhost
            String http = "http://localhost:9090/uploadServlet";
            URL url = new URL(http);
            //生成HttpURLConnection连接
            httpurlconnection = (HttpURLConnection) url.openConnection();
            //生成随机字符串用作X-SID
            randomString = StringUtils.getRandomString(8);
            //向Header中添加X-SID
            httpurlconnection.setRequestProperty("X-SID", randomString);
            // 获得密钥对
            keyPair = RSAUtil.getKeyPair();
            // 获得进行Base64 加密后的公钥和私钥 String
            String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
            publicKeyStr = RSAUtil.getPublicKey(keyPair);
            // 获得原始的私钥
            privateKey = RSAUtil.string2Privatekey(privateKeyStr);
            // 私钥签名
            String sign = RSAUtil.sign(randomString, privateKey);
            //向Header中添加X-Signature
            httpurlconnection.setRequestProperty("X-Signature", sign);
            //传递公钥
            httpurlconnection.setRequestProperty("publicKeyStr", publicKeyStr);
            //定义上传服务器路径
            String path = "http://localhost:9090/uploads/";
            System.out.println("ss" + upload.getOriginalFilename());
            String filename = upload.getOriginalFilename();
            String encodeFilename = URLEncoder.encode(filename, "UTF-8");
            //创建客户端对象
            Client client = Client.create();
            //和图片服务器进行连接 拿到一个webresource资源
            WebResource resource = client.resource(path + encodeFilename);
            //上传文件
            resource.put(upload.getBytes());
            //设置有输出流，默认为false，就是不能传递参数。
            httpurlconnection.setDoOutput(true);
            //设置发送请求的方式。注意：一定要大写
            httpurlconnection.setRequestMethod("POST");
            //设置连接超时的时间。不过不设置，在网络异常的情况下，可能会造成程序僵死而无法继续向下执行，所以一般设置一个超时时间。单位为毫秒
            httpurlconnection.setConnectTimeout(30000);
            //设置输出流。
            writer = new OutputStreamWriter(httpurlconnection.getOutputStream(), "utf-8");
            //传递的参数，中间使用&符号分割。
            writer.write("filename=" + encodeFilename + "&password=4567" + "&filetype=" + upload.getContentType() + "&filesize=" + upload.getSize());
            //用于刷新缓冲流。因为默认她会写入到内存的缓冲流中，到一定的数据量时，才会写入，使用这个命令可以让他立即写入，不然下面就到关闭流了
            writer.flush();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if(writer!=null) {

                    writer.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        BufferedReader reader = null;
        InputStream urlstream = null;
        //用于关闭输出流，关闭之后就不可以输出数据了，所以要使用flush刷新缓冲流
                //获得返回的请求吗。
        try {
            int responseCode = httpurlconnection.getResponseCode();
            //表示请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("OK" + responseCode);
                //获得服务端的输出流。得到返回的数据
                urlstream = httpurlconnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(urlstream));
                String line;
                String tline = "";
                while ((line = reader.readLine()) != null) {
                    tline += line;
                }
                //输出所有的数据
                System.out.println(tline + "hahaha");
                uuid = tline;
                request.setAttribute("uuid", uuid);
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {if(urlstream!=null) {
                    urlstream.close();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            try {
                    if(reader!=null){
                        reader.close();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "success";
    }


    @RequestMapping("/getfileLoad")
    public void getFileLoad(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        HttpURLConnection httpurlconnection = null;
        OutputStreamWriter writer = null;
        try {
            //设置请求的编码方式
            request.setCharacterEncoding("utf-8");
            //设置返回时的编码方式
            response.setContentType("text/html;charset=utf-8");

            System.out.println("test1");
            //设置HTTP连接的URL地址，就是第二个应用的URL。如果在同一个计算机上可以将192.168.1.134变成127.0.0.1或者localhost
            String http = "http://localhost:9090/getFileLoadServlet";
            URL url = new URL(http);
            //生成HttpURLConnection连接
            httpurlconnection = (HttpURLConnection) url.openConnection();
            //向Header中添加X-SID
            httpurlconnection.setRequestProperty("X-SID", randomString);
            // 私钥签名
            String sign = RSAUtil.sign(randomString, privateKey);
            //向Header中添加X-Signature
            httpurlconnection.setRequestProperty("X-Signature", sign);
            //传递公钥
            httpurlconnection.setRequestProperty("publicKeyStr", publicKeyStr);
            httpurlconnection.setRequestProperty("uuid", uuid);
            //设置有输出流，默认为false，就是不能传递参数。
            httpurlconnection.setDoOutput(true);
            //设置发送请求的方式。注意：一定要大写
            httpurlconnection.setRequestMethod("GET");
            //设置连接超时的时间。不过不设置，在网络异常的情况下，可能会造成程序僵死而无法继续向下执行，所以一般设置一个超时时间。单位为毫秒
            httpurlconnection.setConnectTimeout(30000);

            //设置输出流。
            writer = new OutputStreamWriter(httpurlconnection.getOutputStream(), "utf-8");
            //传递的参数，中间使用&符号分割。
            writer.write("uuid=" + uuid);
            //用于刷新缓冲流。因为默认她会写入到内存的缓冲流中，到一定的数据量时，才会写入，使用这个命令可以让他立即写入，不然下面就到关闭流了
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //用于关闭输出流，关闭之后就不可以输出数据了，所以要使用flush刷新缓冲流
            try {
                if (writer != null) {

                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream urlstream = null;
        BufferedReader reader = null;
        try {
            //获得返回的请求吗。
            int responseCode = httpurlconnection.getResponseCode();
            //表示请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("OK" + responseCode);
            } else {
                //跳转403
            }
            //获得服务端的输出流。得到返回的数据
            urlstream = httpurlconnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(urlstream));
            String line;
            String tline = "";
            while ((line = reader.readLine()) != null) {
                tline += line;
            }
            //输出所有的数据
            System.out.println(tline + "我拿到的fuser");
            //将json字符串回传
            response.getWriter().write(tline);
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                if (urlstream != null) {

                    urlstream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader != null) {

                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @RequestMapping("/filedownload")
    public String fileDownload(HttpServletRequest request, HttpServletResponse response) {
        HttpURLConnection httpurlconnection = null;
        OutputStreamWriter writer = null;
        try {
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            System.out.println("下载请求过来了");
            //设置请求的编码方式
            request.setCharacterEncoding("utf-8");
            //设置返回时的编码方式
            response.setContentType("text/html;charset=utf-8");
            System.out.println("test1");
            //设置HTTP连接的URL地址，就是第二个应用的URL。如果在同一个计算机上可以将192.168.1.134变成127.0.0.1或者localhost
            String http = "http://localhost:9090/downloadServlet";
            URL url = new URL(http);
            //生成HttpURLConnection连接
            httpurlconnection = (HttpURLConnection) url.openConnection();
            //向Header中添加X-SID
            httpurlconnection.setRequestProperty("X-SID", randomString);
            // 私钥签名
            String sign = RSAUtil.sign(randomString, privateKey);
            //向Header中添加X-Signature
            httpurlconnection.setRequestProperty("X-Signature", sign);
            //传递公钥
            httpurlconnection.setRequestProperty("publicKeyStr", publicKeyStr);
            httpurlconnection.setRequestProperty("uuid", uuid);
            //设置有输出流，默认为false，就是不能传递参数。
            httpurlconnection.setDoOutput(true);
            //设置发送请求的方式。注意：一定要大写
            httpurlconnection.setRequestMethod("GET");
            //设置连接超时的时间。不过不设置，在网络异常的情况下，可能会造成程序僵死而无法继续向下执行，所以一般设置一个超时时间。单位为毫秒
            httpurlconnection.setConnectTimeout(30000);

            //设置输出流。
            writer = new OutputStreamWriter(httpurlconnection.getOutputStream(), "utf-8");
            //传递的参数，中间使用&符号分割。
            writer.write("uuid=" + uuid);
            //用于刷新缓冲流。因为默认她会写入到内存的缓冲流中，到一定的数据量时，才会写入，使用这个命令可以让他立即写入，不然下面就到关闭流了
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //用于关闭输出流，关闭之后就不可以输出数据了，所以要使用flush刷新缓冲流
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        InputStream urlstream = null;
        BufferedReader reader = null;
        String tline = null;
        try {
            //获得返回的请求吗。
            int responseCode = httpurlconnection.getResponseCode();
            //表示请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("OK" + responseCode);
            } else {
                //跳转410
                return "410";
            }
            //获得服务端的输出流。得到返回的数据
            urlstream = httpurlconnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(urlstream));
            String line;
            tline = "";
            while ((line = reader.readLine()) != null) {
                tline += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlstream != null) {
                    urlstream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream is = null;
        ServletOutputStream outputStream = null;
        try {
            //拿到的json字符串
            Fuser fuser = JSON.parseObject(tline, Fuser.class);
            //拿到数字信封
            String filekey = fuser.getFilekey();
            //拿到存放路径、原始文件名/创建日期和uuid
            String filepath = fuser.getFilepath();
            String filename = fuser.getFilename();
            Date filetime = fuser.getFiletime();
            String date = filetime.toString();
            String uuid = fuser.getUuid();
            System.out.println(filename);
            // 获得原始的公钥和私钥，并以字符串形式打印出来
            byte[] privateDecryBytes = RSAUtil.privateDecrypt(Base64.getDecoder().decode(filekey), privateKey);
            //拿到AES 的key
            String aesKey = new String(privateDecryBytes);
            System.out.println("私钥解密后的原始字符串：" + new String(privateDecryBytes));
            boolean b = AESFileUtil.decryptFile(filepath + uuid, filepath + filename, aesKey);
            //1.获取要下载的文件名
            String downloadFilename = filename;
            //2.读取要下载的文件内容（通过ServletContext对象读取）
            ServletContext servletContext = request.getServletContext();
            //3.回传前告诉客户端返回的数据类型
            String mimeType = servletContext.getMimeType("/uploads/"+date+"/" + downloadFilename);
            System.out.println("要下载的文件类型：" + mimeType);
            response.setContentType(mimeType);
            //判断是否为火狐浏览器
            if (request.getHeader("User-Agent").contains("Firefox")) {
                //是火狐  ?charset?B?xxxxx?= 进行BASE64的编码操作
                response.setHeader("Content-Disposition", "attachment;filename==?UTF-8?B?" + new BASE64Encoder().encode(downloadFilename.getBytes("UTF-8")) + "?=");
            } else {
                //告诉客户端收到的数据用于下载使用（使用响应头） attachment:附件 Content-Disposition：响应头 表示收到数据怎么处理
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFilename, "UTF-8"));
            }
            //将文件写入输入流中
            File file = new File(filepath+downloadFilename);
            FileInputStream fileInputStream = new FileInputStream(file);
            System.out.println("you are"+fileInputStream);
            //4.获取响应的输出流
            outputStream = response.getOutputStream();
            //5.把读到流的全部数据复制写给输出流
            IOUtils.copy(fileInputStream, outputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {

                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

}