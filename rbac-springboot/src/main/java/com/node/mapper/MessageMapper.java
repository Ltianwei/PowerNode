package com.node.mapper;

import com.node.domain.Message;
import com.node.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> getByPage(@Param("userId") Long userId);

    List<Message> selectAll();

    Message selectById(@Param("id") Integer id,@Param("userId") Long userId);

    Message selectNew();
}