package com.node.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  implements Serializable {
    private Integer id;

    private String userName;

    private String password;

    private String email;

    private Integer phone;

    private Date birth;

    public User(Integer id) {
        this.id = id;
    }

    private static final long serialVersionUID = 1L;
}