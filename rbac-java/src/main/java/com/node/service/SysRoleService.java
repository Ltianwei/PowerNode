package com.node.service;

import com.node.common.Page;
import com.node.domain.SysRole;

import java.util.List;

public interface SysRoleService{


    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    Page<SysRole> getByPage(Page page);

    int delete(Long roleId);

    List<Long> getCurrentRoleHasMenuIdsByRoleId(Long roleId);

    List<SysRole> getAllRoles();

    int saveRoleMenu(Long roleId, Long[] menuIds);

    List<Long> getRoleIdsByUserId(Long userId);

    int saveUserRoles(Long userId, Long[] roleIds);
}
