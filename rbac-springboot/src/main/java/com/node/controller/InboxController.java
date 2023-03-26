package com.node.controller;


import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.Message;
import com.node.domain.User;
import com.node.domain.UserMessage;
import com.node.enums.ResultEnums;
import com.node.service.MessageService;
import com.node.service.UserMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/inbox")
public class InboxController extends BaseController{

    @Resource
    private MessageService messageService;

    @Resource
    private UserMessageService userMessageService;

    //查询站内消息记录 分页
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){
        //管理员查询所有消息
        if (getUserInfo().getUserName().equals("admin")){
            page = messageService.selectAll(getUserInfo().getUserId(),page);
            System.out.println("管理员登录");
            return new Result(page);
        }
        page= messageService.getByPage(getUserInfo().getUserId(),page);
       return new Result("查询成功",page);
    }

    //查看消息详情，当点击后为已读状态
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Integer id){
        //更新消息已读状态
        //先查询是否有这个数据 连表
        UserMessage userMessage=userMessageService.select(getUserInfo().getUserId(),id);
        userMessage.setReadStatus(1);
        userMessage.setReadTime(new Date());
        userMessageService.updateByPrimaryKeySelective(userMessage);
        //查询
        Message message= messageService.selectById(id,getUserInfo().getUserId() );
        return new Result("信息查询成功",message);
    }

    //根据消息删除 改变状态
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){

        UserMessage select = userMessageService.select(getUserInfo().getUserId(), id);
        select.setDeleteStatus(1);
        userMessageService.updateByPrimaryKey(select);
        return new Result("已放入垃圾箱");
    }
    //

}
