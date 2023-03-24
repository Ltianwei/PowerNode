package com.node.service;

import com.node.common.Page;
import com.node.domain.SysDictType;

import java.util.List;

public interface SysDictTypeService{


    int deleteByPrimaryKey(Long dictId);

    int insert(SysDictType record);

    int insertSelective(SysDictType record);

    SysDictType selectByPrimaryKey(Long dictId);

    int updateByPrimaryKeySelective(SysDictType record);

    int updateByPrimaryKey(SysDictType record);

    Page sysDictType(Page page);

    List<SysDictType> getAllDicType(Integer status);

   // void refreshCache();
}
