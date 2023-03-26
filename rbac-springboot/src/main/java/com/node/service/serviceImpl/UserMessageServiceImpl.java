package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import com.node.domain.Message;
import com.node.domain.UserMessage;
import com.node.mapper.UserMessageMapper;
import com.node.service.UserMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService{

    @Resource
    private UserMessageMapper userMessageMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userMessageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserMessage record) {
        return userMessageMapper.insert(record);
    }

    @Override
    public int insertSelective(UserMessage record) {
        return userMessageMapper.insertSelective(record);
    }

    @Override
    public UserMessage selectByPrimaryKey(Integer id) {
        return userMessageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserMessage record) {
        return userMessageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserMessage record) {
        return userMessageMapper.updateByPrimaryKey(record);
    }

    @Override
    public UserMessage select(Long id, Integer id1) {
        //根据userid ms id查询是否
        return userMessageMapper.select(id,id1);
    }

    @Override
    public List<UserMessage> selectAll() {
        return userMessageMapper.selectAll();
    }

    //查询已删除
    @Override
    public Page getDelete(Page page,Long id) {
        com.github.pagehelper.Page<Message> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        userMessageMapper.getDelete(id);
        page.setList(startPage.getResult());
        page.setTotalCount(startPage.getTotal());
        return page;
    }



}
