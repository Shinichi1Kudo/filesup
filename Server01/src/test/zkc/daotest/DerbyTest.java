package com.zkc.daotest;//java文件
import com.zkc.utils.JDBCUtils;

import java.nio.file.*;
import java.sql.*;
import java.io.*;
import java.util.*;

/*
 *This program tests that the database and the JDBC driver are correctly configured.
 */

public class DerbyTest{
    public static void main(String[] args) throws IOException{
        try{
            runTest();
        }catch(SQLException ex){
            for(Throwable t:ex)
                t.printStackTrace();
        }

    }
    /**
     *Runs a test by creating a table,adding a value,showing the table contents,and removing the table.
     */
    public static void runTest() throws SQLException,IOException{
        try(Connection conn = getConnection())
        {
            Statement stat = conn.createStatement();
            try(ResultSet result = stat.executeQuery("SELECT * FROM fuser")){
                //将光标移动到下一行，初始在第一行之前
                while(result.next())
                    System.out.println(result.getString("filename"));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     *Gets a connection from the properties specified in the file database.properties.
     *@return the database connection
     */
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return JDBCUtils.getConnection();
    }




}