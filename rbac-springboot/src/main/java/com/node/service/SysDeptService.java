package com.node.service;

import com.node.domain.SysDept;

import java.util.List;

public interface SysDeptService{


    int deleteByPrimaryKey(Long deptId);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Long deptId);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);


    List<SysDept> listDept(SysDept sysDept);

    Long hasChildren(Long deptId);
}
