package com.zkc.dao;

import com.zkc.pojo.Fuser;

/**
 * @author zkc
 * @create 2020-04-23 21:29
 */
public interface FuserDAO {
    //1.根据uuid查询用户信息
    Fuser queryFuserByuuid(String uuid);
    //2.保存用户信息
    void saveUser(Fuser fuser);
}
