package com.node.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.node.annotations.AppPermission;
import com.node.annotations.Log;
import com.node.common.Page;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysUser;
import com.node.enums.BusinessType;
import com.node.enums.ResultEnums;
import com.node.enums.StateEnums;
import com.node.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


@RestController
@RequestMapping("/sysUser")
public class sysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    /*
     * 分页查询
     * */
    @AppPermission("system:user:query")
    @PostMapping("/getByPage")
    @Log(title = "用户管理",businessType = BusinessType.SEARCH)
    public Result getByPage(@RequestBody Page<SysUser> page) {
        Page<SysUser> page1 = sysUserService.getByPage(page);
        return new Result("查询成功", page);
    }

    /*
     * 增加用户
     * */
    @AppPermission("system:user:add")
    @PostMapping("/save")
    @Log(title = "用户管理",businessType = BusinessType.INSERT)
    public Result save(@RequestBody SysUser sysUser) {
        /*
         * 判断用户名是否存在
         * */
        SysUser su = sysUserService.getUserByName(sysUser.getUserName());
        if (su != null) {
            return new Result(ResultEnums.ERROR, "用户已存在");
        }
        sysUser.setCreateBy(getUserName());
        sysUser.setCreateTime(new Date());
        sysUser.setPassword(DigestUtil.md5Hex(CoreConstant.DEFAULT_PASSWORD));
        sysUser.setDelFlag(StateEnums.NOT_DELETED.getCode());
        sysUser.setUserType(StateEnums.USER_TYPE_NORMAL.getCode());
        sysUserService.insertSelective(sysUser);
        return new Result(ResultEnums.SUCCESS, "添加成功");
    }

    /*
     * 修改用户
     * */
    @AppPermission("system:user:update")
    @PutMapping("/update")
    @Log(title = "用户管理",businessType = BusinessType.UPDATE)
    public Result update(@RequestBody SysUser sysUser) {
        // 根据传来的名字判断是否存在当前的名字的用户
        SysUser userByName = sysUserService.getUserByNameAndId(sysUser);
        if (userByName!=null){
            return new Result(ResultEnums.ERROR,"当前用户名已存在");
        }
        sysUser.setUpdateBy(getUserName());
        sysUser.setUpdateTime(new Date());
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("更新用户数据成功");
    }
    /*
    * 删除用户
    * */
    @AppPermission("system:user:remove")
    @DeleteMapping("/delete/{userId}")
    @Log(title = "用户管理",businessType = BusinessType.DELETE)
    public Result delete(@PathVariable Long userId){
        //将用户的状态改为删除
        SysUser sysUser = sysUserService.selectByPrimaryKey(userId);
        sysUser.setDelFlag(StateEnums.DELETED.getCode());
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("用户删除成功");
    }
    /*
    * 重置密码
    * */
    @AppPermission("system:user:resetPwd")
    @GetMapping("/resetPwd/{userId}")
    @Log(title = "用户管理",businessType = BusinessType.UPDATE)
    public Result resetPwd(@PathVariable Long userId){
        SysUser sysUser = sysUserService.selectByPrimaryKey(userId);
        if (sysUser==null){
            return new Result(ResultEnums.ERROR,"用户不存在");
        }
        sysUser.setPassword(DigestUtil.md5Hex(CoreConstant.DEFAULT_PASSWORD));
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("重置密码成功");
    }
    /*
    * 根据用户id查询
    * */
    @GetMapping("/get/{userId}")
    @Log(title = "用户管理",businessType = BusinessType.SEARCH)
    public Result get(@PathVariable Long userId){
        SysUser sysUser = sysUserService.selectByPrimaryKey(userId);
        return new Result("查询成功",sysUser);
    }

}
