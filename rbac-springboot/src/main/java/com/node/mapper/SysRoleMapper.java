package com.node.mapper;

import com.node.domain.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;


public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> selectAllRole(@Param("params") Map<String,Object> params);

    int delete(Long roleId);

    List<Long> getCurrentRoleHasMenuIdsByRoleId(Long roleId);

    List<SysRole> getAllRoles();
}