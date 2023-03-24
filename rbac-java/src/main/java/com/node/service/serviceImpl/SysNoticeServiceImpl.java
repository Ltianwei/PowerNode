package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysNoticeMapper;
import com.node.domain.SysNotice;
import com.node.service.SysNoticeService;

import java.util.List;

@Service
public class SysNoticeServiceImpl implements SysNoticeService{

    @Resource
    private SysNoticeMapper sysNoticeMapper;

    @Override
    public int deleteByPrimaryKey(Long noticeId) {
        return sysNoticeMapper.deleteByPrimaryKey(noticeId);
    }

    @Override
    public int insert(SysNotice record) {
        return sysNoticeMapper.insert(record);
    }

    @Override
    public int insertSelective(SysNotice record) {
        return sysNoticeMapper.insertSelective(record);
    }

    @Override
    public SysNotice selectByPrimaryKey(Long noticeId) {
        return sysNoticeMapper.selectByPrimaryKey(noticeId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysNotice record) {
        return sysNoticeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysNotice record) {
        return sysNoticeMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<SysNotice> getByPage(Page page) {
        com.github.pagehelper.Page<SysNotice> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<SysNotice> list=sysNoticeMapper.getByPage(page.getParams());
        page.setTotalCount(startPage.getTotal());
        page.setList(startPage.getResult());
        return page;
    }

}
