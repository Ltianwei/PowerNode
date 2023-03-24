package com.node.service;

import com.node.common.Page;
import com.node.domain.SysOperLog;

import java.util.List;

public interface SysOperLogService{


    int deleteByPrimaryKey(Long operId);

    int insert(SysOperLog record);

    int insertSelective(SysOperLog record);

    SysOperLog selectByPrimaryKey(Long operId);

    int updateByPrimaryKeySelective(SysOperLog record);

    int updateByPrimaryKey(SysOperLog record);

    Page getByPage(Page page);

    List<Long> selectAll();

}
