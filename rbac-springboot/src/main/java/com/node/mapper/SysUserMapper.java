package com.node.mapper;

import com.node.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser queryByUserNameAndPassword(@Param("username") String username,@Param("password") String password);

    List<String> queryPermsByUserId(@Param("userId") Long userId);

    List<SysUser> getByPage(@Param("params") Map<String, Object> params);

    SysUser getUserByName(@Param("userName") String userName);

    SysUser getUserByNameAndId(@Param("sysUser") SysUser sysUser);
}