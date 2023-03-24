package com.node.mapper;

import com.node.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDictDataMapper {
    int deleteByPrimaryKey(Long dictCode);

    int insert(SysDictData record);

    int insertSelective(SysDictData record);

    SysDictData selectByPrimaryKey(Long dictCode);

    int updateByPrimaryKeySelective(SysDictData record);

    int updateByPrimaryKey(SysDictData record);

    List<SysDictData> getByPage(@Param("params") Map params);

    List<SysDictData> selectByType(@Param("dictType") String dictType);
}