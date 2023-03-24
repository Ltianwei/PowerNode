package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.node.domain.SysRoleMenu;
import com.node.mapper.SysRoleMenuMapper;
import com.node.service.SysRoleMenuService;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public int deleteByPrimaryKey(Long roleId, Long menuId) {
        return sysRoleMenuMapper.deleteByPrimaryKey(roleId, menuId);
    }

    @Override
    public int insert(SysRoleMenu record) {
        return sysRoleMenuMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRoleMenu record) {
        return sysRoleMenuMapper.insertSelective(record);
    }





}
