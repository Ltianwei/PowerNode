package com.node.controller;

import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysNotice;
import com.node.service.SysNoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.relation.RelationSupport;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysNotice")
public class SysNoticeController extends BaseController{

    @Resource
    private SysNoticeService sysNoticeService;

    /*
    * 分页
    * */
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){
      Page<SysNotice> page1= sysNoticeService.getByPage(page);
      return new Result("查询成功",page1);
    }

    /*
    * 添加
    * */
    @PostMapping("/save")
    public Result add(@RequestBody SysNotice sysNotice){
        sysNotice.setCreateBy(getUserName());
        sysNotice.setCreateTime(new Date());
        sysNoticeService.insertSelective(sysNotice);
        return new Result("添加成功");
    }

    /*
    * 更新
    * */
    @PutMapping("/update")
    public Result update(@RequestBody SysNotice sysNotice){
        sysNotice.setUpdateBy(getUserName());
        sysNotice.setUpdateTime(new Date());
        sysNoticeService.updateByPrimaryKeySelective(sysNotice);
        return new Result("添加成功");
    }

    /*
    * 删除
    * */
    @DeleteMapping("/delete/{noticeId}")
    public Result delete(@PathVariable Long noticeId){
        sysNoticeService.deleteByPrimaryKey(noticeId);
        return new Result("删除成功");
    }
    /*
    * 根据id查询
    * */
    @GetMapping("/get/{noticeId}")
    public Result get(@PathVariable Long noticeId){
        SysNotice sysNotice = sysNoticeService.selectByPrimaryKey(noticeId);
        return new Result("查询成功",sysNotice);
    }

}
