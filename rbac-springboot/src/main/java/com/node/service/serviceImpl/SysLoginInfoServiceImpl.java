package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.node.mapper.SysLoginInfoMapper;
import com.node.domain.SysLoginInfo;
import com.node.service.SysLoginInfoService;

@Service
public class SysLoginInfoServiceImpl implements SysLoginInfoService {

    @Resource
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long infoId) {
        return sysLoginInfoMapper.deleteByPrimaryKey(infoId);
    }

    @Override
    public int insert(SysLoginInfo record) {
        return sysLoginInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(SysLoginInfo record) {
        return sysLoginInfoMapper.insertSelective(record);
    }

    @Override
    public SysLoginInfo selectByPrimaryKey(Long infoId) {
        return sysLoginInfoMapper.selectByPrimaryKey(infoId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysLoginInfo record) {
        return sysLoginInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysLoginInfo record) {
        return sysLoginInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<SysLoginInfo> getByPage(Page page) {
        com.github.pagehelper.Page<SysLoginInfo> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysLoginInfoMapper.getByPage(page.getParams());
        page.setList(startPage.getResult());
        page.setTotalCount(startPage.getTotal());
        return page;
    }

    @Override
    public int deleteAll() {
        return sysLoginInfoMapper.deleteAll();
    }


}
