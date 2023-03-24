package com.node.service;

import com.node.common.Page;
import com.node.domain.SysNotice;
public interface SysNoticeService{


    int deleteByPrimaryKey(Long noticeId);

    int insert(SysNotice record);

    int insertSelective(SysNotice record);

    SysNotice selectByPrimaryKey(Long noticeId);

    int updateByPrimaryKeySelective(SysNotice record);

    int updateByPrimaryKey(SysNotice record);

    Page<SysNotice> getByPage(Page page);
}
