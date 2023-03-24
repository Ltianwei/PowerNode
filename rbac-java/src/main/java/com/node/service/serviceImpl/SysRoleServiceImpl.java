package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import com.node.domain.SysRoleMenu;
import com.node.mapper.SysRoleMenuMapper;
import com.node.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.node.mapper.SysRoleMapper;
import com.node.domain.SysRole;
import com.node.service.SysRoleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long roleId) {
        return sysRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(SysRole record) {
        return sysRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRole record) {
        return sysRoleMapper.insertSelective(record);
    }

    @Override
    public SysRole selectByPrimaryKey(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysRole record) {
        return sysRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysRole record) {
        return sysRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<SysRole> getByPage(Page page) {
        //分页查询所有的数据
        com.github.pagehelper.Page<SysRole> rolePage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysRoleMapper.selectAllRole(page.getParams());
        page.setTotalCount(rolePage.getTotal());
        page.setList(rolePage.getResult());
        return page;
    }

    @Override
    public int delete(Long roleId) {
        return sysRoleMapper.delete(roleId);
    }

    @Override
    public List<Long> getCurrentRoleHasMenuIdsByRoleId(Long roleId) {
        //根据id查询菜单权限
        List<Long> list = sysRoleMapper.getCurrentRoleHasMenuIdsByRoleId(roleId);
        return list;
    }

    @Override
    public List<SysRole> getAllRoles() {
        return sysRoleMapper.getAllRoles();
    }

    @Override
    public int saveRoleMenu(Long roleId, Long[] menuIds) {
        //先将用户菜单删除 后批量添加用户菜单
        //判断角色菜单表中是否包含当前角色的菜单
        Integer count = sysRoleMenuMapper.selectByRoleId(roleId);
        if (count > 0) {
            //如果存在删除该角色下所有的菜单
            sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
        }
        //批量添加菜单 先将数组转为集合
        List<Long> menuIdList = Arrays.asList(menuIds);
        return sysRoleMenuMapper.saveAllMenu(roleId, menuIdList);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        //根据用户id查询角色id
        List<Long> list = sysRoleMenuMapper.getRoleIdsByUserId(userId);
        return list;
    }

    @Override
    public int saveUserRoles(Long userId, Long[] roleIds) {
        //先删除对应的角色 在重新添加
        Integer count=sysUserRoleMapper.select(userId);
        if (count>0){
            sysUserRoleMapper.deleteByUserId(userId);
        }
       return sysUserRoleMapper.saveUserRoles(userId,roleIds);

    }

}
