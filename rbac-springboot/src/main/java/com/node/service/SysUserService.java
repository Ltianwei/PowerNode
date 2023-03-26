package com.node.service;

import com.node.common.Page;
import com.node.domain.SysUser;

import java.util.List;

public interface SysUserService{


    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser queryByUserNameAndPassword(String username, String password);

    List<String> queryPermsByUserId(Long userId);

    Page<SysUser> getByPage(Page<SysUser> page);

    SysUser getUserByName(String userName);

    SysUser getUserByNameAndId(SysUser sysUser);

    List<Long> getAllUserId();

    List<SysUser> getAllUser();

}
