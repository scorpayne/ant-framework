package com.payne.framework.helper;

import com.payne.framework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 获取被框架管理的所有Bean类
 * Created by dengpeng on 2017/6/20.
 */
public final class BeanHelper {
    //定义一个map，存放的是class与其实例的映射集合
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static{
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for(Class<?> cls:beanClassSet){
            Object bean = ReflectionUtils.newInstance(cls);
            BEAN_MAP.put(cls,bean);
        }
    }

    /**
     * 获取bean容器
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取bean
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class："+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置bean   为了获取所有目标类及其被拦截的切面类，并通过ProxyManager来创建代理对象，放入到bean map中
     */
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}
