package com.node.common;

import com.node.enums.ResultEnums;
import lombok.Data;

import java.util.HashMap;

/**
 * 统一返回包装类
     继承HashMap 目的  动态追加新的成员变量
 */
@Data
public class Result<T> extends HashMap<String,Object> {

    private Integer code;

    private String msg;

    private T data;




    public Result() {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = ResultEnums.SUCCESS.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(String msg) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(T data) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = ResultEnums.SUCCESS.getMsg();
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }

    public Result(String msg, T data) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = msg;
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(ResultEnums resultEnum, String msg) {
        this.code = resultEnum.getCode();
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(ResultEnums resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }
    public Result(ResultEnums resultEnum, Object data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",data);
    }

}