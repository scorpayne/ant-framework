package com.payne.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 反射类，让框架来生成bean实例，控制反转。
 * Created by dengpeng on 2017/6/20.
 */
public final class ReflectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance = cls.newInstance();
        }catch (Exception e){
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object...args){
        Object result;
        try{
            method.setAccessible(true); //setAccessible是启用和禁用访问
            result = method.invoke(obj, args);
        }catch (Exception e){
            LOGGER.error("invoke method failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value){
        field.setAccessible(true);
        try {
            field.set(obj,value);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }
}
