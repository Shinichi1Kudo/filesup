package com.zkc.dao.impl;

import com.zkc.dao.BaseDAO;
import com.zkc.dao.FuserDAO;
import com.zkc.pojo.Fuser;
import com.zkc.utils.JDBCUtils;

import java.awt.print.Book;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @create 2020-04-23 21:33
 */
public class FuserDAOImpl extends BaseDAO<Fuser> implements FuserDAO{

    @Override
    public Fuser queryFuserByuuid(String uuid) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select * from fuser where uuid=?";
        Fuser fuser = queryForOne(Fuser.class,conn,sql, uuid);
        JDBCUtils.closeResource(conn,null);
        return fuser;
    }

    @Override
    public void saveUser(Fuser fuser) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into fuser(filename,filesize,filetype,filetime,filepath,filekey,uuid)values(?,?,?,?,?,?,?)";
        update(conn,sql,fuser.getFilename(),fuser.getFilesize(),fuser.getFiletype(),fuser.getFiletime(),fuser.getFilepath(),fuser.getFilekey(),fuser.getUuid());
        JDBCUtils.closeResource(conn,null);
    }

}
