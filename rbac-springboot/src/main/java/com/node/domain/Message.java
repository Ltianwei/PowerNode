package com.node.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
    * 主键
    */
    private Long id;

    private String title;

    private String content;

    private Long senderId;

    private Long recipientId;

    private Integer readStatus;

    private Integer deleteStatus;

    private Date createTime;

    private Date readTime;

    private Integer issystem;

    //private UserMessage userMessage;

    private static final long serialVersionUID = 1L;
}