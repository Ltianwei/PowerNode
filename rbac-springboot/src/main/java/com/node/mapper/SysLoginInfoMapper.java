package com.node.mapper;

import com.node.common.Page;
import com.node.domain.SysLoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysLoginInfoMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(SysLoginInfo record);

    int insertSelective(SysLoginInfo record);

    SysLoginInfo selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(SysLoginInfo record);

    int updateByPrimaryKey(SysLoginInfo record);

    List<SysLoginInfo> getByPage(@Param("map") Map map);

    int deleteAll();
}