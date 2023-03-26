package com.node.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.node.common.Page;
import com.node.domain.Message;
import com.node.domain.User;
import com.node.mapper.MessageMapper;
import com.node.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    private MessageMapper messageMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Message record) {
        return messageMapper.insert(record);
    }

    @Override
    public int insertSelective(Message record) {
        return messageMapper.insertSelective(record);
    }

    @Override
    public Message selectByPrimaryKey(Integer id) {
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Message record) {
        return messageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Message record) {
        return messageMapper.updateByPrimaryKey(record);
    }
    @Override
    public Page getByPage(Long userId, Page page) {
        com.github.pagehelper.Page<Message> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        messageMapper.getByPage(userId);
        page.setTotalCount(startPage.getTotal());
        page.setList(startPage.getResult());
        return page;
    }

    @Override
    public Page selectAll(Long userId, Page page) {
        com.github.pagehelper.Page<Message> startPage = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
         messageMapper.selectAll();
        page.setTotalCount(startPage.getTotal());
        page.setList(startPage.getResult());
        return page;
    }

    @Override
    public Message selectById(Integer id,Long userId) {
        //根据id查询详细信息
        return  messageMapper.selectById(id,userId);
    }

    @Override
    public Message selectNew() {
        return messageMapper.selectNew();
    }

}
