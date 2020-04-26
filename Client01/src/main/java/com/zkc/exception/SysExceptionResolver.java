package com.zkc.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 * @author zkc
 * @create 2020-04-20 12:34
 */
public class SysExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //获取到异常对象
        SysException e = null;
        if(e instanceof SysException){
            e = (SysException)ex;
        }else{
            e = new SysException("。。。啊啊啊...程序猿小哥在紧急抢修~~ ~");
        }
        //创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg",e.getMsg());
        modelAndView.setViewName("/WEB-INF/410.jsp");
        return modelAndView;
    }
}
