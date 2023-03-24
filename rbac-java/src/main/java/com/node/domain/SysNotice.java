package com.node.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 通知公告表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysNotice implements Serializable {
    /**
    * 公告ID
    */
    private Long noticeId;

    /**
    * 公告标题
    */
    private String noticeTitle;

    /**
    * 公告类型（1通知 2公告）
    */
    private Integer noticeType;

    /**
    * 公告内容
    */
    private String noticeContent;

    /**
    * 公告状态（0正常 1关闭）
    */
    private Integer status;

    /**
    * 创建者
    */
    private String createBy;

    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新者
    */
    private String updateBy;

    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
    * 备注
    */
    private String remark;

    private static final long serialVersionUID = 1L;
}