package com.zkc.utils;

/**
 * @author zkc
 * @create 2020-04-23 15:56
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import javax.sql.DataSource;

//import com.alibaba.druid.pool.DruidDataSourceFactory;


public class JDBCUtils {
    public static Connection getConnection(){
        Class clazz = null;
        try {
            clazz = Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Driver driver = null;
        try {
            driver = (Driver) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String url = "jdbc:derby://localhost:1527/files;create=true";
        String username = "dbuser4";
        String password = "secret4";
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String drivers = "corg.apache.derby.jdbc.ClientDriver";
        //为了适应那些不能自动注册的数据库驱动程序
        if(drivers != null)
            //这种方式可以提供多个驱动器，使用冒号分割
            System.setProperty("jdbc.drivers",drivers);
        try {
            return  DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void closeResource(Connection conn,Statement ps){
        //关闭资源操作
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void closeResource(Connection conn,Statement ps,ResultSet rs){
        //关闭资源操作
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if(rs!=null){
                rs.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}