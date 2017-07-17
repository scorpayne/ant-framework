package com.payne.framework.helper;

import com.payne.framework.annotation.Inject;
import com.payne.framework.util.ArrayUtil;
import com.payne.framework.util.MapUtil;
import com.payne.framework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 此类，是为了遍历bean容器，并实现依赖注入
 * Created by dengpeng on 2017/6/20.
 */
public final class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (MapUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取beanClass的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field field : beanFields) {
                        if (field.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = field.getType();
                            Object beanFieldInstance = BeanHelper.getBean(beanFieldClass);//bean容器中获取该成员类的实例
                            if(beanFieldInstance!=null){
                                ReflectionUtils.setField(beanInstance,field,beanFieldInstance);//把实例赋值到该成员变量中
                            }
                        }
                    }
                }
            }
        }
    }
}
