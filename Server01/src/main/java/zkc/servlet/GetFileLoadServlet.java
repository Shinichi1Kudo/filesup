package com.zkc.servlet;

import com.alibaba.fastjson.JSONArray;
import com.zkc.pojo.Fuser;
import com.zkc.service.FuserService;
import com.zkc.service.impl.FuserServiceImpl;
import com.zkc.utils.RSAUtil;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author zkc
 * @create 2020-04-24 21:18
 */
public class GetFileLoadServlet extends HttpServlet {
    private FuserService fuserService = new FuserServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("utf-8");
        System.out.println("test22");
        //验证签名
        String header1 = request.getHeader("X-Signature");
        String header2 = request.getHeader("publicKeyStr");
        String header3 = request.getHeader("X-SID");
        System.out.println("验证签名X-SID："+header3);
        System.out.println("验证签名publicKeyStr："+header2);
        System.out.println("验证签名X-Signature："+header1);
        try {
            PublicKey publicKey = RSAUtil.string2PublicKey(header2);
            System.out.println("验证成功"+RSAUtil.verify(header3, header1, publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("我执行了");
        //获得客户端uuid
        String uuid=request.getParameter("uuid");
        System.out.println("我的"+uuid);
        uuid=uuid.substring(5);
        System.out.println(uuid);
        //通过uuid查询数据库信息
        Fuser fuser = fuserService.queryFuser(uuid);
        System.out.println(fuser);
        Object obj = JSONArray.toJSON(fuser);
        String json = obj.toString();
        System.out.println("将fuser对象转成json:" + json);
        //向HTTP发送方返回响应数据
        if("123".equals(uuid)){
            response.getWriter().write("{\"falg\":\"success\"}");
        }else{
            response.getOutputStream().write(json.getBytes("gb2312"));
        }


    }
}
