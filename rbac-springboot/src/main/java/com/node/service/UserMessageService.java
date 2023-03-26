package com.node.service;

import com.node.common.Page;
import com.node.domain.UserMessage;

import java.util.List;

public interface UserMessageService{


    int deleteByPrimaryKey(Integer id);

    int insert(UserMessage record);

    int insertSelective(UserMessage record);

    UserMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMessage record);

    int updateByPrimaryKey(UserMessage record);

    UserMessage select(Long id, Integer id1);

    List<UserMessage> selectAll();

    Page getDelete(Page page,Long id);


}
