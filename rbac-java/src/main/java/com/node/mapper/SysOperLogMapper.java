package com.node.mapper;

import com.node.domain.SysOperLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOperLogMapper {
    int deleteByPrimaryKey(Long operId);

    int insert(SysOperLog record);

    int insertSelective(SysOperLog record);

    SysOperLog selectByPrimaryKey(Long operId);

    int updateByPrimaryKeySelective(SysOperLog record);

    int updateByPrimaryKey(SysOperLog record);

    List<SysOperLog> getByPage(@Param("params") Map params);

    List<Long> selectAll();

}