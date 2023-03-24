package com.node.controller;


import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysLoginInfo;
import com.node.service.SysLoginInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sysLoginInfo")
public class SysLoginInfoController {

    @Resource
    private SysLoginInfoService sysLoginInfoService;

    /*
    * 分页查询
    * */
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){
       Page<SysLoginInfo> page1= sysLoginInfoService.getByPage(page);
       return new Result("查询成功",page);
    }
    /*
    * 根据id删除登录日志
    * */
    @DeleteMapping("/delete/{infoId}")
    public Result delete(@PathVariable Long infoId){
        sysLoginInfoService.deleteByPrimaryKey(infoId);
        return new Result("删除成功");
    }
    /*
    * 批量删除登录日志
    * */
    @DeleteMapping("/batchDelete/{ids}")
    public Result batchDelete(@PathVariable Long[] ids){
        if (ids==null||ids.length==0){
            return new Result("输入参数有误");
        }
        for (Long id : ids) {
            sysLoginInfoService.deleteByPrimaryKey(id);
        }
        return new Result("删除成功");
    }
    /*
    * 全部删除
    * */
    @DeleteMapping("/cleanLoginInfo")
    public Result cleanLoginInfo(){
        sysLoginInfoService.deleteAll();
        return new Result("删除成功");
    }
}
