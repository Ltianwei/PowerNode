package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysOperLogMapper;
import com.node.domain.SysOperLog;
import com.node.service.SysOperLogService;

import java.util.List;

@Service
public class SysOperLogServiceImpl implements SysOperLogService{

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public int deleteByPrimaryKey(Long operId) {
        return sysOperLogMapper.deleteByPrimaryKey(operId);
    }

    @Override
    public int insert(SysOperLog record) {
        return sysOperLogMapper.insert(record);
    }

    @Override
    public int insertSelective(SysOperLog record) {
        return sysOperLogMapper.insertSelective(record);
    }

    @Override
    public SysOperLog selectByPrimaryKey(Long operId) {
        return sysOperLogMapper.selectByPrimaryKey(operId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysOperLog record) {
        return sysOperLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysOperLog record) {
        return sysOperLogMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page getByPage(Page page) {
        com.github.pagehelper.Page<SysOperLog> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysOperLogMapper.getByPage(page.getParams());
        page.setList(startPage.getResult());
        page.setTotalCount(startPage.getTotal());
        return page;
    }

    @Override
    public List<Long> selectAll() {

        return sysOperLogMapper.selectAll();
    }

}
