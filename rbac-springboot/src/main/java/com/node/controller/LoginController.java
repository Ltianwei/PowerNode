package com.node.controller;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.digest.DigestUtil;
import com.node.annotations.AppPermission;
import com.node.async.AsyncSysLoginLogInfoService;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysLoginInfo;
import com.node.domain.SysUser;
import com.node.domain.User;
import com.node.enums.ResultEnums;
import com.node.service.SysUserService;
import com.node.utils.HttpUtils;
import com.node.utils.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RequestMapping
@RestController
public class LoginController {

    @Resource
    private SysUserService userService;

    //注意要交给IOC
    @Resource
    private AsyncSysLoginLogInfoService asyncSysLoginLogInfoService;

    /*
    * 登陆方法
    * */
    //增加登录日志的功能
    SysLoginInfo sysLoginInfo=new SysLoginInfo();
    @PostMapping(CoreConstant.LOGIN_URL)
    public Result login(String username,String password,String code){

        try {
            /******************************************/
            sysLoginInfo.setUserName(username);
            //设置登录的浏览器
            sysLoginInfo.setBrowser(HttpUtils.getBrowserName(ServletUtils.getRequest()));
            //设置登录浏览器的版本
            sysLoginInfo.setOs(HttpUtils.getOsName(ServletUtils.getRequest()));
            //设置状态
            sysLoginInfo.setStatus(1);
            //设置登录时间
            sysLoginInfo.setLoginTime(new Date());
            //设置ip
            sysLoginInfo.setIpAddr(HttpUtils.getIpAddr(ServletUtils.getRequest()));
            //设置登录信息 根据条件判断

            HttpSession session = ServletUtils.getSession();
            //先判断验证码的正确性 获取session中的验证码对象
            CircleCaptcha captcha = (CircleCaptcha) session.getAttribute(CoreConstant.CAPTCHA_KEY);
            //判断验证码是否存在
            if (captcha ==null ){
                //这里是验证码错误 设置登录信息
                sysLoginInfo.setMsg(ResultEnums.CAPTCHA_ERROR.getMsg());

                return new Result(ResultEnums.CAPTCHA_ERROR);
            }
            //用captcha中的方法判断获取的参数是否与验证码一致
            boolean flag = captcha.verify(code);
            if (!flag){
                //这里是验证码错误 设置登录信息
                sysLoginInfo.setMsg(ResultEnums.CAPTCHA_ERROR.getMsg());

                return new Result(ResultEnums.CAPTCHA_ERROR);
            }
            //将session中的验证码删除
            session.removeAttribute(CoreConstant.CAPTCHA_KEY);
            //密码需要加密 DigestUtil.md5Hex()
            password=DigestUtil.md5Hex(password);
            SysUser sysUser= userService.queryByUserNameAndPassword(username,password);
            if (sysUser == null){
                //这里是账号密码错误 设置登录信息
                sysLoginInfo.setMsg(ResultEnums.USER_PASSWORD_ERROR.getMsg());

                return new Result(ResultEnums.USER_PASSWORD_ERROR);
            }
            session.setAttribute(CoreConstant.SESSION_KEY,sysUser);
            //设置状态
            sysLoginInfo.setStatus(0);
            //这个代码是站内信的
            //将用户信息存入session
            ServletUtils.getSession().setAttribute("userMsg",new User(Math.toIntExact(sysUser.getUserId())));
            /*-------------------------------------*/
            //设置登录成功
            sysLoginInfo.setMsg("登录成功");
        } finally {
            //异步保存 该方法会自动保存提交查找登录信息
           asyncSysLoginLogInfoService.saveLoginLogInfo(sysLoginInfo);
        }
        return new Result(ResultEnums.SUCCESS);
    }

    /*
     * 根据用户id 获取相应的权限值 perms permissions
     * 以及用户信息 通过map返回
     * */
   // @AppPermission("system:user:info")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        Map<String, Object> map = new HashMap<>();
        Long userId = BaseController.getUserInfo().getUserId();
        List<String> permissions = userService.queryPermsByUserId(userId);

        //将用户信息的权限存储到会话中
        ServletUtils.getSession().setAttribute(CoreConstant.PERMISSIONS_KEY,permissions);

        //可以将用户的密码设为空字符
        SysUser userInfo = BaseController.getUserInfo();
        userInfo.setPassword("");
        map.put(CoreConstant.SESSION_KEY,userInfo);
        map.put("permissions",permissions);
        return new Result(ResultEnums.SUCCESS,map);
    }

    /*
    * 退出登录
    * */
    @GetMapping("/logout")
    public Result logout(){
        //将session中的存储的用户信息清空 关闭session可以清除所有信息
        ServletUtils.getSession().invalidate();
        return new Result(ResultEnums.SUCCESS,"退出登录成功！");
    }
}
