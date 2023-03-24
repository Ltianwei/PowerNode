package com.node.mapper;

import com.node.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    Integer select(Long userId);

    int deleteByUserId(Long userId);

    int saveUserRoles(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
}