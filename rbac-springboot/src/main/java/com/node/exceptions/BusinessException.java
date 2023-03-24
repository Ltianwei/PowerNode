package com.node.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessException extends RuntimeException{

    private String message="操作异常";
    private Integer code=3000;
    private Object data;

    public BusinessException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, Object data) {
        //将异常信息传输到父类 运行期异常 可以报出自定义的信息
        super(message);
        this.data = data;
    }

    public BusinessException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
