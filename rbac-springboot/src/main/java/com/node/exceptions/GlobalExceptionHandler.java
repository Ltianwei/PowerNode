package com.node.exceptions;

/*
* 定义全局异常处理器 所有的异常都会捕获
* */

import com.node.common.Result;
import com.node.enums.ResultEnums;
import org.springframework.web.bind.annotation.*;
//全局异常 ControllerAdvice已经将该类交给了SpringMvc
@ControllerAdvice
public class GlobalExceptionHandler {

    //定义异常处理的类 实现接口ExceptionHandler
    @ExceptionHandler
    @ResponseBody//定义返回值为Json
    public Result exceptionHandler(Exception ex){
        if (ex instanceof BusinessException){
            //如果是运行期异常 打印相应的信息
            BusinessException be= (BusinessException) ex;
            be.printStackTrace();
            return new Result(ResultEnums.ERROR,be.getMessage());
        }
        ex.printStackTrace();
        //通过json返回前端错误原因
        return new Result(ResultEnums.ERROR,ex.getMessage());
    }
}
