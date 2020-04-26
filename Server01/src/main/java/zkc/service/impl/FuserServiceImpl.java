package com.zkc.service.impl;

import com.zkc.dao.impl.FuserDAOImpl;
import com.zkc.pojo.Fuser;
import com.zkc.service.FuserService;

/**
 * @author zkc
 * @create 2020-04-23 22:04
 */
public class FuserServiceImpl implements FuserService{
    private FuserDAOImpl fuserDAO = new FuserDAOImpl();

    @Override
    public Fuser queryFuser(String uuid) {
        Fuser fuser = fuserDAO.queryFuserByuuid(uuid);
        return fuser;
    }

    @Override
    public void saveUser(Fuser fuser) {
        fuserDAO.saveUser(fuser);
    }
}
