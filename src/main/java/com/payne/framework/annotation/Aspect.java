package com.payne.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by dengpeng on 2017/6/21.
 */
@Target(ElementType.TYPE)  //该注解只能应用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 注解
     */
    Class<? extends Annotation> value();
}
