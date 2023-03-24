package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysDictDataMapper;
import com.node.domain.SysDictData;
import com.node.service.SysDictDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SysDictDataServiceImpl implements SysDictDataService{

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public int deleteByPrimaryKey(Long dictCode) {
        return sysDictDataMapper.deleteByPrimaryKey(dictCode);
    }

    @Override
    public int insert(SysDictData record) {
        return sysDictDataMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDictData record) {
        return sysDictDataMapper.insertSelective(record);
    }

    @Override
    public SysDictData selectByPrimaryKey(Long dictCode) {
        return sysDictDataMapper.selectByPrimaryKey(dictCode);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDictData record) {
        return sysDictDataMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDictData record) {
        return sysDictDataMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page getByPage(Page page) {

        /*System.out.println(page.getParams().get("dictType"));*/
        com.github.pagehelper.Page pages = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysDictDataMapper.getByPage(page.getParams());
        page.setTotalCount(pages.getTotal());
        page.setList(pages.getResult());
        return page;
    }

    @Override
    public List<SysDictData> selectByType(String dictType) {
        List<SysDictData> list=sysDictDataMapper.selectByType(dictType);
        return list;
    }

}
