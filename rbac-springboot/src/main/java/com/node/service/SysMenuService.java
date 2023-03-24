package com.node.service;

import com.node.domain.SysMenu;
import com.node.domain.SysUser;
import com.node.vo.RouterVo;

import java.util.List;

public interface SysMenuService{


    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectByMenuNameAndStatus(SysMenu sysMenu, SysUser user);

    int hasChildren(Long menuId);

    int delete(Long menuId);

    List<SysMenu> listMenuMC(SysUser sysUser);

    List<RouterVo> getRouters(SysUser user);
}
