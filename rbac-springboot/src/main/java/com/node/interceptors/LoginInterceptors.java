package com.node.interceptors;

import com.alibaba.fastjson.JSON;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysUser;
import com.node.enums.ResultEnums;
import com.node.utils.ServletUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
* 编写些监听器 当用户未登录时 要求用户登录
* */
public class LoginInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登录 session是否有相应的信息 如果有放行
        SysUser sysUser = (SysUser) ServletUtils.getSession().getAttribute(CoreConstant.SESSION_KEY);
        if(sysUser == null){

            /** 如果用户未登录 json方式返回result*/

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;character=utf-8");
            response.getWriter().write(JSON.toJSONString(new Result<>(ResultEnums.USER_NOT_LOGIN)));
            return false;
        }
        //如果登录 放行 mvc xml中需要配置
        return true;
    }
}
