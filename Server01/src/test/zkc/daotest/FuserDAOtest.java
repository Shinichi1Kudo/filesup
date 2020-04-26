package com.zkc.daotest;

import com.zkc.dao.impl.FuserDAOImpl;
import com.zkc.pojo.Fuser;
import com.zkc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;

/**
 * @author zkc
 * @create 2020-04-23 21:50
 */
public class FuserDAOtest {
    @Test
    public void test1(){
        //增加一个用户
        Connection conn = JDBCUtils.getConnection();
        FuserDAOImpl fuserDAO = new FuserDAOImpl();
        fuserDAO.saveUser(new Fuser("bb.jpeg","1000","jpeg",new Date(new java.util.Date().getTime()),"lujing","dasddaas22221dd2d","1232132131221"));
        JDBCUtils.closeResource(conn,null);
        //存储成功
    }
}
