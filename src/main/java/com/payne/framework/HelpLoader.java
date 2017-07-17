package com.payne.framework;

import com.payne.framework.annotation.Controller;
import com.payne.framework.helper.*;
import com.payne.framework.util.ClassUtils;

/**
 * Created by dengpeng on 2017/6/21.
 */
public final class HelpLoader {
    public static void init(){
//        AopHelper要在IocHelper前加载，先获取代理对象，才能进行依赖注入
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls:classList){
            ClassUtils.loadClass(cls.getName(),false);
        }
    }
}
