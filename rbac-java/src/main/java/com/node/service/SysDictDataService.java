package com.node.service;

import com.node.common.Page;
import com.node.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDictDataService{


    int deleteByPrimaryKey(Long dictCode);

    int insert(SysDictData record);

    int insertSelective(SysDictData record);

    SysDictData selectByPrimaryKey(Long dictCode);

    int updateByPrimaryKeySelective(SysDictData record);

    int updateByPrimaryKey(SysDictData record);

    Page<List<SysDictData>> getByPage(Page page);

    List<SysDictData> selectByType( String dictType);
}
