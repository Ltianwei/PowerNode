package com.node.controller;


import cn.hutool.captcha.CircleCaptcha;
import com.alibaba.fastjson.JSON;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.enums.ResultEnums;
import com.node.utils.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



/*
 * 短信验证码获取
 * */

@RestController
public class CaptchaController {

    @GetMapping(CoreConstant.CAPTCHA_URL)
    public Result getCaptcha(){
        /*因为要获取session 可以集合到一个封装类中*/
        //湖区hutool的验证码生成方法
        CircleCaptcha captcha = new CircleCaptcha(160,80,4,20);
        //测试获取的验证码
        System.out.println(captcha.getCode());
        String base64Data = captcha.getImageBase64Data();
        //将当前验证码放入session对象 后续根据这个来判断是否登录 以及验证码是否正确
        ServletUtils.getSession().setAttribute(CoreConstant.CAPTCHA_KEY,captcha);
        return new Result(ResultEnums.SUCCESS,JSON.toJSON(base64Data));
    }

}
