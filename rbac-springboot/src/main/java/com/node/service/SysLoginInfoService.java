package com.node.service;

import com.node.common.Page;
import com.node.domain.SysLoginInfo;
public interface SysLoginInfoService{


    int deleteByPrimaryKey(Long infoId);

    int insert(SysLoginInfo record);

    int insertSelective(SysLoginInfo record);

    SysLoginInfo selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(SysLoginInfo record);

    int updateByPrimaryKey(SysLoginInfo record);

    Page<SysLoginInfo> getByPage(Page page);

    int deleteAll();
}
