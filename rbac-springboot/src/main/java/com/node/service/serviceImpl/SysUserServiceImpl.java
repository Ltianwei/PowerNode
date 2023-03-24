package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysUserMapper;
import com.node.domain.SysUser;
import com.node.service.SysUserService;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService{

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public int deleteByPrimaryKey(Long userId) {
        return sysUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(SysUser record) {
        return sysUserMapper.insert(record);
    }

    @Override
    public int insertSelective(SysUser record) {
        return sysUserMapper.insertSelective(record);
    }

    @Override
    public SysUser selectByPrimaryKey(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysUser record) {
        return sysUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public SysUser queryByUserNameAndPassword(String username, String password) {
        return sysUserMapper.queryByUserNameAndPassword(username,password);
    }

    @Override
    public List<String> queryPermsByUserId(Long userId) {
        return sysUserMapper.queryPermsByUserId(userId);
    }

    @Override
    public Page<SysUser> getByPage(Page<SysUser> page) {
        com.github.pagehelper.Page<SysUser> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysUserMapper.getByPage(page.getParams());
        page.setList(startPage.getResult());
        page.setTotalCount(startPage.getTotal());
        return page;
    }

    @Override
    public SysUser getUserByName(String userName) {
        //根据用户名判断用户是否存在
      SysUser sysUser= sysUserMapper.getUserByName(userName);
        return sysUser;
    }

    @Override
    public SysUser getUserByNameAndId(SysUser sysUser) {
        return  sysUserMapper.getUserByNameAndId(sysUser);

    }

}
