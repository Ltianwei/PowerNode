package com.node.mapper;

import com.node.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);


    Integer selectByRoleId(Long roleId);

    Integer deleteRoleMenuByRoleId(Long roleId);

    int saveAllMenu(@Param("roleId") Long roleId,@Param("menuIdList") List<Long> menuIdList);

    List<Long> getRoleIdsByUserId(Long userId);

}