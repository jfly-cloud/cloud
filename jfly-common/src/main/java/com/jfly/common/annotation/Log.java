package com.jfly.common.annotation;
import com.jfly.common.enums.BusinessType;
import com.jfly.common.enums.OperatorType;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MIDDLE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;


}