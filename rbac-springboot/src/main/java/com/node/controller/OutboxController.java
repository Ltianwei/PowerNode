package com.node.controller;


import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.Message;
import com.node.domain.User;
import com.node.domain.UserMessage;
import com.node.service.MessageService;
import com.node.service.SysUserService;
import com.node.service.UserMessageService;
import com.node.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/outbox")
public class OutboxController extends BaseController{

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private SysUserService sysUserService;

    //发送邮件
    @PostMapping("/send")
    public Result send(@RequestBody Message message){
        message.setCreateTime(new Date());
        message.setDeleteStatus(0);
        message.setSenderId(getUserInfo().getUserId());
        messageService.insertSelective(message);
        System.out.println("执行完毕");
        return new Result("发送成功",message);
    }
    //发送后执行方法

    @GetMapping("/inUserMsg")
    public Result inUserMsg(){
        //获取最大的
        Message message=messageService.selectNew();
        System.out.println("-------------------------------");
        System.out.println(message.getIssystem());
        if (message.getIssystem().equals(1)){

            //获取所有用户
           List<Long> users= sysUserService.getAllUserId();
           users.forEach(u->{
               userMessageService.insertSelective(new UserMessage(null,u,message.getId(),0,0,null));
           });
        }else {
            userMessageService.insertSelective(new UserMessage(null,message.getRecipientId(), message.getId(),  0, 0, null));
        }
        return new Result("后台添加成功");
    }

    //获取当前用户是否是管理员
    @GetMapping("/getUserIsAdmin")
    public Result getUserIsAdmin(){
        Integer userId = Math.toIntExact(getUserInfo().getUserId());
        boolean b = userId.equals(1);
        //是为0 不是为1
        return new Result(b?0:1);
    }

    //查询所有user
    @GetMapping("/getAllUser")
    public Result getAllUser(){
        List<User> users= userService.selectAll();
        return new Result(users);
    }





}
