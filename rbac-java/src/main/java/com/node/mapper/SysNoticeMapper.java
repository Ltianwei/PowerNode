package com.node.mapper;

import com.node.domain.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysNoticeMapper {
    int deleteByPrimaryKey(Long noticeId);

    int insert(SysNotice record);

    int insertSelective(SysNotice record);

    SysNotice selectByPrimaryKey(Long noticeId);

    int updateByPrimaryKeySelective(SysNotice record);

    int updateByPrimaryKey(SysNotice record);

    List<SysNotice> getByPage(@Param("params") Map map);
}