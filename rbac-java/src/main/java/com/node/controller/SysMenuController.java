package com.node.controller;


import com.node.annotations.AppPermission;
import com.node.common.Result;
import com.node.domain.SysMenu;
import com.node.domain.SysUser;
import com.node.enums.ResultEnums;
import com.node.service.SysMenuService;
import com.node.vo.RouterVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

    @Resource
    public SysMenuService sysMenuService;

    /*
     * 根据当前角色 查询菜单 以及传输的参数查询
     * */
    @AppPermission("system:menu:query")
    @GetMapping("/listMenu")
    public Result listMenu(SysMenu sysMenu) {
        SysUser user = getUserInfo();
        List<SysMenu> list = sysMenuService.selectByMenuNameAndStatus(sysMenu, user);
        return new Result(ResultEnums.SUCCESS.getCode(), "查询成功", list);
    }

    /*
     * 添加菜单
     * */
    @AppPermission("system:menu:add")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        //添加菜单 需要自定义编号 并发情况防止编号重复 添加错误 引入自旋锁
        // sysMenu.setCreateBy(getUserName());
        // sysMenu.setCreateTime(new Date());
        sysMenu.setStatus(0);
        sysMenuService.insertSelective(sysMenu);
        return new Result("菜单添加成功");
    }

    /*
     * 更新操作
     * */
    @AppPermission("system:menu:update")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenu.setUpdateTime(new Date());
        sysMenu.setUpdateBy(getUserName());
        sysMenuService.updateByPrimaryKeySelective(sysMenu);
        return new Result("菜单更新成功");
    }

    /*
     * 删除操作
     * */
    @AppPermission("system:menu:remove")
    @DeleteMapping("/delete/{menuId}")
    public Result delete(@PathVariable Long menuId) {
        //执行删除时 要进行判断是否包含子菜单 如果存在不允许删除
        Integer count = sysMenuService.hasChildren(menuId);
        if (count > 0) {
            return new Result(ResultEnums.ERROR, "该菜单存在子菜单，无法删除");
        } else {
            //具体业务中 删除是改变业务的状态 而不是真的删除
            sysMenuService.delete(menuId);
            return new Result("删除成功");
        }
    }

    /*
     * 根据id查询菜单
     * */
    @GetMapping("/get/{menuId}")
    public Result getById(@PathVariable Long menuId) {
        SysMenu sysMenu = sysMenuService.selectByPrimaryKey(menuId);
        return new Result("查询成功", sysMenu);
    }

    /*
     * 查询所有的菜单和目录
     * (一级以及二级标签)
     * */

    @GetMapping("/listMenuMC")
    public Result listMenuMC() {
        //需要判断当前的角色是否是超级管理员
        List<SysMenu> list = sysMenuService.listMenuMC(getUserInfo());
        return new Result("查询成功", list);
    }

    /*
     *
     * 获取动态路由
     * 用前端路由的方式展现该用户下所拥有的路由
     * */
    @GetMapping("/getRouters")
    public Result<List<RouterVo>> getRouters() {
        //传入用户信息
        List<RouterVo> list = sysMenuService.getRouters(getUserInfo());
        return new Result<>(list);
    }


}
