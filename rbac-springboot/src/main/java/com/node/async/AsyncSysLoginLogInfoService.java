package com.node.async;

import com.node.domain.SysLoginInfo;
import com.node.service.SysLoginInfoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * 登陆日志处理的异步工具类
 */

@Component
@Async //支持异步处理
@EnableAsync //开启异步机制
public class AsyncSysLoginLogInfoService {

    @Resource
    private SysLoginInfoService loginInfoService;


    /**
     * 添加登陆日志
     */
    public void saveLoginLogInfo(SysLoginInfo loginInfo){
        String ipAddr = loginInfo.getIpAddr();
        //处理IP
        if (ipAddr!=null){
            if(ipAddr.equalsIgnoreCase("127.0.0.1")||ipAddr.equalsIgnoreCase("localhost")){
                loginInfo.setLoginLocation("本地");
            }else{
                //可以去网上找对应的IP查询API，去发请求得到IP的所在地址
            }
        }
        this.loginInfoService.insertSelective(loginInfo);
    }


}
