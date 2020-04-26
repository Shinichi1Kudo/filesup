package com.zkc.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zkc.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

public abstract class BaseDAO<T> {
    private static QueryRunner runner = new QueryRunner();//操作查询（反射出异常很奇怪）
    private Class<T> clazz= null;
    {
        //获取当前BaseDAO的子类继承的父类中的泛型
        //这个type：BaseDAO<T>
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        //强转成带参数的type
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        //获取父类泛型参数
        Type[] typeArguments = paramType.getActualTypeArguments();
        clazz = (Class<T>)typeArguments[0];//拿到泛型第一个参数
    }

    //增删改
    public void update(Connection conn,String sql,Object ...args){
        PreparedStatement ps=null;
        try {
            ps = conn.prepareStatement(sql);

            for(int i=0;i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(null, ps);

        }
    }
    //返回一个查询对象的javaBean
    public <T> T queryForOne(Class<T> clazz,Connection conn,String sql,Object...args) {
        try {
            T query = runner.query(conn, sql, new BeanHandler<T>(clazz), args);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    很奇怪 自己写的代码跑MySQL没有问题 Derby跑了就报
// java.lang.NoSuchFieldException: FILENAME 异常
//    字段名没写错，也声明的private方法 方法调用也是getDeclaredField 就很奇怪
//    public <T> T Query(Connection conn,Class<T> clazz,String sql,Object...args){
//        PreparedStatement ps=null;
//        ResultSet rs=null;
//        try {
//            ps = conn.prepareStatement(sql);
//            for(int i =0;i < args.length;i++){
//                ps.setObject(i+1, args[i]);
//            }
//            rs = ps.executeQuery();
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnCount = rsmd.getColumnCount();
//            if(rs.next()){
//                T t = clazz.newInstance();
//                for(int i=0;i<columnCount;i++){
//                    //拿到字段值
//                    Object columnValue = rs.getObject(i+1);
//                    //拿到字段名
//                    String columnName = rsmd.getColumnLabel(i+1);
//
//                    //通过反射赋值
//                    Field field = clazz.getDeclaredField(columnName);
//                    field.setAccessible(true);
//                    field.set(t, columnValue);
//                }
//                return t;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            JDBCUtils.closeResource(null, ps, rs);
//
//        }
//        return null;
//
//    }






    //返回多个对象的查询
    public List<T> LotsQuery(Connection conn,String sql,Object...args){
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i =0;i < args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<T>();
            while(rs.next()){
                T t = clazz.newInstance();
                for(int i=0;i<columnCount;i++){
                    //拿到字段值
                    Object columnValue = rs.getObject(i+1);
                    //拿到字段名
                    String columnName = rsmd.getColumnLabel(i+1);

                    //通过反射赋值
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(null, ps, rs);

        }
        return null;

    }
    //(查询特殊值的通用方法)返回的类型是不确定的可能String int
    public <E> E getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);//只有一条数据
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{

            JDBCUtils.closeResource(null, ps,rs);
        }
        return null;
    }
}
