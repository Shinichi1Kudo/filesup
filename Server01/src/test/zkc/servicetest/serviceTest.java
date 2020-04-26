package com.zkc.servicetest;

import com.zkc.dao.impl.FuserDAOImpl;
import com.zkc.pojo.Fuser;
import com.zkc.service.FuserService;
import com.zkc.service.impl.FuserServiceImpl;
import com.zkc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author zkc
 * @create 2020-04-24 22:06
 */
public class serviceTest {
    //查询用户
    @Test
    public void test1(){
        Connection conn = JDBCUtils.getConnection();
        FuserService fuserService = new FuserServiceImpl();
        Fuser fuser = fuserService.queryFuser("1232132131221");
        System.out.println(fuser);
        JDBCUtils.closeResource(conn,null);
        //Fuser{filename='bb.jpeg', filesize='1000', filetype='jpeg',
        // filetime=2020-04-25, filepath='lujing', filekey='dasddaas221dd2d'}
        //查询到数据库用户信息
    }
}
