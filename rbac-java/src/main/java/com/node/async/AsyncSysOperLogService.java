package com.node.async;


import com.node.domain.SysOperLog;
import com.node.service.SysOperLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 记录用户操作日志的异步处理器

 */
@Component
@Async
@EnableAsync
public class AsyncSysOperLogService {

    @Resource
    private SysOperLogService operLogService;


    /**
     * 添加登陆日志
     */
    public void saveOperLogService(SysOperLog operLog){
        String operIp = operLog.getOperIp();
        //处理IP
        if (operIp!=null){
            if(operIp.equalsIgnoreCase("127.0.0.1")||operIp.equalsIgnoreCase("localhost")){
                operLog.setOperLocation("本地");
            }else{
                //大家可以去网上找对应的IP查询API，去发请求得到IP的所在地址
            }
        }
        this.operLogService.insertSelective(operLog);
    }

}
