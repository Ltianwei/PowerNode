package com.node.controller;

import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysUser;
import com.node.enums.ResultEnums;
import com.node.utils.ServletUtils;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    /*
    * 快捷获取session中的存入用户的信息
    * */
    public static SysUser getUserInfo(){
        SysUser user = (SysUser) ServletUtils.getSession().getAttribute(CoreConstant.SESSION_KEY);
        return user;
    }
    /*
    * 获取用户名
    * */
    public static String getUserName(){
        return getUserInfo().getUserName();
    }
}
