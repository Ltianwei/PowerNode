package com.node.annotations;

import com.node.enums.BusinessType;
import java.lang.annotation.*;


@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    String title() default "";
    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;
    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
}
