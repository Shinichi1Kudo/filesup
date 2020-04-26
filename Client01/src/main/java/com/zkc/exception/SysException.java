package com.zkc.exception;

/**
 * @author zkc
 * @create 2020-04-20 12:28
 */
public class SysException extends Exception{
    private String msg;
    static final long serialVersionUID = -3387523124348L;
    public SysException(){

    }
    public SysException(String msg){
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
