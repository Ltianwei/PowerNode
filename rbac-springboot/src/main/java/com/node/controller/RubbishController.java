package com.node.controller;


import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.UserMessage;
import com.node.service.MessageService;
import com.node.service.UserMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rubbish")
public class RubbishController extends BaseController{

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private MessageService messageService;

    //分页查询删除的信息
    @PostMapping("/getDelete")
    public Result getDelete(Page page){
        Page page1= userMessageService.getDelete(page, getUserInfo().getUserId());
      return new Result(page1);
    }

    //删除信息
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userMessageService.deleteByPrimaryKey(id);
        return new Result("删除成功"+id);
    }

}
