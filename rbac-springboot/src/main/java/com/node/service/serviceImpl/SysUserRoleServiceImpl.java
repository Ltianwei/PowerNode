package com.node.service.serviceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysUserRoleMapper;
import com.node.domain.SysUserRole;
import com.node.service.SysUserRoleService;
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService{

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long userId,Long roleId) {
        return sysUserRoleMapper.deleteByPrimaryKey(userId,roleId);
    }

    @Override
    public int insert(SysUserRole record) {
        return sysUserRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(SysUserRole record) {
        return sysUserRoleMapper.insertSelective(record);
    }

}
