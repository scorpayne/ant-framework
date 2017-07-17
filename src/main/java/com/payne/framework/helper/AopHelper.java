package com.payne.framework.helper;

import com.payne.framework.annotation.Aspect;
import com.payne.framework.annotation.Service;
import com.payne.framework.annotation.Transaction;
import com.payne.framework.aspect.ControllerAspect;
import com.payne.framework.proxy.AspectProxy;
import com.payne.framework.proxy.Proxy;
import com.payne.framework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by dengpeng on 2017/6/21.
 */
public final class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static{
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

            for(Map.Entry<Class<?>, List<Proxy>> targetEntry:targetMap.entrySet()){
                Object proxy = ProxyManager.createProxy(targetEntry.getKey(), targetEntry.getValue());
                BeanHelper.setBean(targetEntry.getKey(),proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure",e);
        }

    }

    /**
     *
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        //判断aspect注解中设置的注解类是不是除aspect类型以外的类，不是的话放入目标类的集合中
        if(annotation != null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类（切面类）与目标类集合之间的映射关系，一个代理是可以代理多个目标类的
     * 代理类得满足两个条件，实现抽象类AspectProxy，并且有Aspect注解
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        //获取所有的代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for(Class<?> proxyClass:proxyClassSet){
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
        return proxyMap;
    }

    /**
     * 获取目标类与代理对象列表之间的映射关系
     * @return
     * @throws Exception
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();

        for(Map.Entry<Class<?>,Set<Class<?>>> proxyEntry:proxyMap.entrySet()){
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for(Class<?> targetClass:targetClassSet){
                Proxy proxy = (Proxy)proxyClass.newInstance();
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);//这里不怕proxy重复吗
                }else{
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }


    /**
     * 用于普通切面代理
     */
    private static void addAspectProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(Aspect.class);
        for(Class<?> proxyClass:proxyClassSet){
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
    }

    /**
     * 用于事务代理
     */
    private static void addTransactionProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetBySuper(Service.class);
        proxyMap.put(Transaction.class,serviceClassSet);
    }
}
