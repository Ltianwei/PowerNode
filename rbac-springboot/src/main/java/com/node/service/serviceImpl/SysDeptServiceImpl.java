package com.node.service.serviceImpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysDeptMapper;
import com.node.domain.SysDept;
import com.node.service.SysDeptService;

import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService{

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public int deleteByPrimaryKey(Long deptId) {
        return sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    @Override
    public int insert(SysDept record) {
        return sysDeptMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDept record) {
        return sysDeptMapper.insertSelective(record);
    }

    @Override
    public SysDept selectByPrimaryKey(Long deptId) {
        return sysDeptMapper.selectByPrimaryKey(deptId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDept record) {
        return sysDeptMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDept record) {
        return sysDeptMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysDept> listDept(SysDept sysDept) {
        List<SysDept> list= sysDeptMapper.listDept(sysDept);

        return list;
    }

    @Override
    public Long hasChildren(Long deptId) {

        return sysDeptMapper.hasChildren(deptId);
    }

}
