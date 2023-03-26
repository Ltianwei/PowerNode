package com.node.mapper;

import com.node.domain.Message;
import com.node.domain.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMessage record);

    int insertSelective(UserMessage record);

    UserMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMessage record);

    int updateByPrimaryKey(UserMessage record);

    UserMessage select(@Param("userId") Long id,@Param("messageId") Integer id1);

    List<UserMessage> selectAll();

    List<Message> getDelete(Long id);
}