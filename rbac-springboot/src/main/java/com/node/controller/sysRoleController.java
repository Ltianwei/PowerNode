
package com.node.controller;

import com.node.annotations.AppPermission;
import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysRole;
import com.node.enums.StateEnums;
import com.node.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysRole")
public class sysRoleController extends BaseController {


    @Resource
    private SysRoleService sysRoleService;



    /* 分页查询*/
    @AppPermission("system:role:query")
    @PostMapping("/getByPage")
    public Result<Page> getByPage(@RequestBody Page page) {
        Page<SysRole> list = sysRoleService.getByPage(page);
        return new Result<>("查询成功", list);
    }

    /* 添加角色
     * 添加创建人 创建时间 未删除
     */
    @AppPermission("system:role:add")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole sysRole) {
        sysRole.setCreateBy(getUserName());
        sysRole.setCreateTime(new Date());
        sysRole.setDelFlag(StateEnums.NOT_DELETED.getCode());
        sysRoleService.insertSelective(sysRole);
        return new Result("添加成功");
    }


    /* 修改角色 更新人 更新时间
     */
    @AppPermission("system:role:update")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        sysRole.setUpdateBy(getUserName());
        sysRole.setUpdateTime(new Date());
        sysRoleService.updateByPrimaryKeySelective(sysRole);
        return new Result("更新成功");
    }


    /* 删除角色 更改角色的删除状态 并不是真的删除
     */
    @AppPermission("system:role:remove")
    @DeleteMapping("/delete/{roleId}")
    public Result delete(@PathVariable Long roleId) {
        sysRoleService.delete(roleId);
        return new Result("删除成功");
    }


    /* 根据传过来的id执行查找
     */

    @GetMapping("/get/{roleId}")
    public Result get(@PathVariable Long roleId) {
        SysRole sysRole = sysRoleService.selectByPrimaryKey(roleId);
        return new Result("查询成功", sysRole);
    }


    /* * 根据当前的角色查找相对应的角色id 两表联查
     * */
    @GetMapping("/getCurrentRoleHasMenuIdsByRoleId/{roleId}")
    public Result getCurrentRoleHasMenuIdsByRoleId(@PathVariable Long roleId) {
        List<Long> list = sysRoleService.getCurrentRoleHasMenuIdsByRoleId(roleId);
        return new Result("查询成功", list);
    }


    /* 保存菜单与角色之间的关系
     * 分配权限时使用 先将所有的权限删除 再添加 防止判断逻辑复杂
     * 参数当前角色id 以及当前角色分配的菜单数组 批量添加
     */
    @AppPermission("system:role:selectPermission")
    @PostMapping("/saveRoleMenu")
    public Result saveRoleMen(Long roleId, Long[] menuIds) {
        System.out.println(Arrays.toString(menuIds));
        System.out.println("-------------");
        sysRoleService.saveRoleMenu(roleId, menuIds);
        return new Result("菜单分配成功");
    }

    /* 查询所有的角色 不分页
     */

    @GetMapping("/getAllRoles")
    public Result getAllRoles() {
        List<SysRole> list = sysRoleService.getAllRoles();
        return new Result("查询成功", list);
    }

    /*
     * 根据用户id查询对应的角色id
     * */
    @GetMapping("/getRoleIdsByUserId/{userId}")
    public Result getRoleIdsByUserId(@PathVariable Long userId) {
        List<Long> list = sysRoleService.getRoleIdsByUserId(userId);
        return new Result("查询成功",list);
    }

    /*
    * 保存用户角色之间的关系
    * 添加用户对应的角色
    * */
    @AppPermission("system:user:selectRole")
    @PostMapping("/saveUserRoles")
    public Result saveUserRoles(Long userId,Long[] roleIds){
        sysRoleService.saveUserRoles(userId,roleIds);
        return new Result("用户角色添加成功");
    }


}

