package com.node.controller;


import com.node.annotations.AppPermission;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysDept;
import com.node.enums.ResultEnums;
import com.node.enums.StateEnums;
import com.node.service.SysDeptService;
import com.node.service.serviceImpl.SysDeptServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysDept")
public class SysDeptController extends BaseController{

    @Resource
    private SysDeptService sysDeptService;

    /*
    * 根据传输的信息 查询所有部门
    * */
    @AppPermission("system:dept:query")
    @GetMapping("/listDept")
    public Result listDept(SysDept sysDept){
       List<SysDept> list= sysDeptService.listDept(sysDept);
       return new Result(ResultEnums.SUCCESS,list);
    }

    /*
    * 添加部门
    * */
    @AppPermission("system:dept:add")
    @PostMapping("/save")
   public Result save(@RequestBody SysDept sysDept){
       // sysDept.setCreateBy(getUserName());
        sysDept.setCreateTime(new Date());
        sysDept.setDelFlag(StateEnums.NOT_DELETED.getCode());
        sysDeptService.insert(sysDept);
        return new Result(ResultEnums.SUCCESS,"添加成功");
    }

    /*
     *修改部门
     */
    @AppPermission("system:dept:edit")
    @PutMapping("/update")
    public Result update(@RequestBody SysDept sysDept){
        //sysDept.setUpdateBy(getUserName());
        sysDept.setUpdateTime(new Date());
        sysDeptService.updateByPrimaryKey(sysDept);
        return new Result(ResultEnums.SUCCESS,"更新成功");
    }
    /*
    * 删除部门
    * */
    @AppPermission("system:dept:remove")
    @DeleteMapping("/delete/{deptId}")
    public Result delete(@PathVariable Long deptId){
        //判断是否存在子部门 如果存在不允许删除
        Long i=sysDeptService.hasChildren(deptId);
        if (i>0){
            return new Result(ResultEnums.PARAMS_ERROR,"该部门存在子部门，无法删除");
        }
        //删除本质是将删除状态改变 而不是将数据删除
        SysDept sysDept = sysDeptService.selectByPrimaryKey(deptId);
        sysDept.setDelFlag(StateEnums.DELETED.getCode());
        sysDeptService.updateByPrimaryKeySelective(sysDept);
        return new Result(ResultEnums.SUCCESS,"删除成功");
    }

    /*
    * 根据id查询部门
    * */
    @GetMapping("/get/{deptId}")
    public Result getDeptById(@PathVariable Long deptId){
        SysDept sysDept = sysDeptService.selectByPrimaryKey(deptId);
        return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",sysDept);
    }


}
