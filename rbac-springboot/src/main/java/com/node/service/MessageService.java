package com.node.service;

import com.node.common.Page;
import com.node.domain.Message;
import com.node.domain.User;

import java.util.List;

public interface MessageService{


    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    Page getByPage(Long userId, Page page);

    Page selectAll(Long userId, Page page);

    Message selectById(Integer id,Long userId);

    Message selectNew();
}
