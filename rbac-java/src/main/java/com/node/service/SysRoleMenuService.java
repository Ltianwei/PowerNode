package com.node.service;

import com.node.common.Page;
import com.node.domain.SysRole;
import com.node.domain.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuService{


    int deleteByPrimaryKey(Long roleId,Long menuId);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    //Page<SysRole> getByPage(Page page);
}
