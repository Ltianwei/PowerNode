package com.node.mapper;

import com.node.domain.SysDictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDictTypeMapper {
    int deleteByPrimaryKey(Long dictId);

    int insert(SysDictType record);

    int insertSelective(SysDictType record);

    SysDictType selectByPrimaryKey(Long dictId);

    int updateByPrimaryKeySelective(SysDictType record);

    int updateByPrimaryKey(SysDictType record);

    List<SysDictType> queryByDictNameAndStatus(@Param("params") Map params);

    List<SysDictType> getAllDicType(@Param("status") Integer status);
}