package com.node.aspects;

import com.node.annotations.AppPermission;
import com.node.constants.CoreConstant;
import com.node.controller.BaseController;
import com.node.exceptions.BusinessException;
import com.node.utils.ServletUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


import java.util.List;

/*
* 创建aop切面类 处理注解类
* 定义一个前置增强类
* */
@Component //交给spring
@Aspect //切面
@EnableAspectJAutoProxy //开启注解版的AOP功能
public class PermissionAspect extends BaseController {

    /*给注解增加切面 在有注解的方法或者类进行增强处理*/

    @Before(value = "@annotation(appPermission)")
    public void before(AppPermission appPermission){
        System.out.println("权限判断开始。。。");
        //获取注解的值
        String value = appPermission.value();
        //判断注解的值是否在当前角色拥有的权限之中
       /* //如果当前角色是超管 直接放行 获取当前角色的类型
        if ( getUserInfo().getUserType().equals(CoreConstant.))*/
        List<String> list = (List<String>) ServletUtils.getSession().getAttribute(CoreConstant.PERMISSIONS_KEY);
        if (list.isEmpty()|| !list.contains(value)){
            //如果当前权限列表为空 或者用户的权限列表不包含想要达到的权限类 抛出自定义运行期异常
            throw new BusinessException(CoreConstant.NO_PERMISSION);

        }
    }
}
