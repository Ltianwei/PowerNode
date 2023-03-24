package com.node.service;

import com.node.domain.SysUserRole;
public interface SysUserRoleService{


    int deleteByPrimaryKey(Long userId,Long roleId);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

}
