package com.node.annotations;


/*
* 权限注解
* 创建自定义注解 用来交给aop判断哪些类是需要判断权限
* */


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AppPermission {
     String value();
}
