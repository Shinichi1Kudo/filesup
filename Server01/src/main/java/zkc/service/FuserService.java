package com.zkc.service;

import com.zkc.pojo.Fuser;

/**
 * @author zkc
 * @create 2020-04-23 22:02
 */
public interface FuserService {
    //1.查询用户信息
    Fuser queryFuser(String uuid);
    //2.保存用户信息
    void saveUser(Fuser fuser);
}
