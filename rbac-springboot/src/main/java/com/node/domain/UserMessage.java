package com.node.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage implements Serializable {
    private Long id;

    private Long userId;

    private Long messageId;

    private Integer readStatus;

    private Integer deleteStatus;

    private Date readTime;

    private static final long serialVersionUID = 1L;
}