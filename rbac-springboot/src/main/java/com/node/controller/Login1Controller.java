package com.node.controller;


import com.node.common.Result;
import com.node.domain.User;
import com.node.enums.ResultEnums;
import com.node.service.UserService;
import com.node.utils.ServletUtils;
import com.qiniu.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class Login1Controller {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    public Result login(@RequestBody User user){
        //查询用户名密码是否正确
        if (user==null || StringUtils.isNullOrEmpty(user.getUserName())||StringUtils.isNullOrEmpty(user.getPassword())){
            return new Result(ResultEnums.USER_PASSWORD_ERROR);
        }
        user=userService.login(user);
        if (user==null){
            return new Result(ResultEnums.USER_PASSWORD_ERROR);
        }
        //将用户信息存入session
        ServletUtils.getSession().setAttribute("userMsg",user);
        return new Result("登录成功！");

    }

}
