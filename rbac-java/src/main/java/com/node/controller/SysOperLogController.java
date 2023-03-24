package com.node.controller;


import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysOperLog;
import com.node.enums.ResultEnums;
import com.node.service.SysOperLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sysOperLog")
public class SysOperLogController {

    @Resource
    private SysOperLogService sysOperLogService;

    /*
     * 分页
     * */
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page) {

        page = sysOperLogService.getByPage(page);

        return new Result("查询成功", page);
    }

    /*
     * 根据id删除
     * */
    @DeleteMapping("/delete/{operId}")
    public Result delete(@PathVariable Long operId) {
        sysOperLogService.deleteByPrimaryKey(operId);
        return new Result("删除成功");
    }

    /*
     * 批量删除
     * */
    @DeleteMapping("/batchDelete/{ids}")
    public Result batchDelete(@PathVariable List<Long> ids) {

        if (ids == null) {
            return new Result(ResultEnums.ERROR, "参数不能为空");
        }
        ids.forEach(id -> {
            sysOperLogService.deleteByPrimaryKey(id);
        });

        return new Result("删除成功");
    }

    /*
     * 删除全部
     * */
    @DeleteMapping("/cleanOperLog")
    public Result cleanOperLog() {
        //查询所有
       List<Long> list= sysOperLogService.selectAll();
      list.forEach(id->{
          sysOperLogService.deleteByPrimaryKey(id);
      });

      return new Result("所有日志已经清空");
    }

    /*
    * 根据id查询
    * */
    @GetMapping("/get/{operId}")
    public Result get(@PathVariable Long operId){
        SysOperLog sysOperLog = sysOperLogService.selectByPrimaryKey(operId);
        return new Result(sysOperLog);
    }


}
