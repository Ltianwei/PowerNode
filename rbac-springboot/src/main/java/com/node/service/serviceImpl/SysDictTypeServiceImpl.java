package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import com.node.service.CacheService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.node.mapper.SysDictTypeMapper;
import com.node.domain.SysDictType;
import com.node.service.SysDictTypeService;

import java.util.List;

@Service
public class SysDictTypeServiceImpl implements SysDictTypeService{

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

  /*  @Resource
    private CacheService cacheService;*/

    @Override
    public int deleteByPrimaryKey(Long dictId) {
        return sysDictTypeMapper.deleteByPrimaryKey(dictId);
    }

    @Override
    public int insert(SysDictType record) {
        return sysDictTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDictType record) {
        return sysDictTypeMapper.insertSelective(record);
    }

    @Override
    public SysDictType selectByPrimaryKey(Long dictId) {
        return sysDictTypeMapper.selectByPrimaryKey(dictId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDictType record) {
        return sysDictTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDictType record) {
        return sysDictTypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page sysDictType(Page page) {
        com.github.pagehelper.Page<Object> page1 = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        sysDictTypeMapper.queryByDictNameAndStatus( page.getParams());
        page.setList(page1.getResult());
        page.setTotalCount(page1.getTotal());
        return page;
    }

    @Override
    public List<SysDictType> getAllDicType(Integer status) {

        return sysDictTypeMapper.getAllDicType(status);
    }

    /*@Override
    public void refreshCache() {
        cacheService.refreshCache();
    }*/

}
