package com.payne.framework.helper;

import com.payne.framework.annotation.Action;
import com.payne.framework.bean.Handler;
import com.payne.framework.bean.Request;
import com.payne.framework.util.ArrayUtil;
import com.payne.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dengpeng on 2017/6/21.
 */
public final class ControllerHelper {
    /**
     * 用于存放请求和处理的映射集合
     */
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static{
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)){
            for(Class<?> controllerClass:controllerClassSet){
                Method[] controllerMethods = controllerClass.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(controllerMethods)){
                    for(Method method:controllerMethods){
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value(); //即括号里面的值
                            if(mapping.matches("\\w+:/\\w")){
                                String[] array = mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array) && array.length==2){
                                    //获取请求方法及路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);

                                    Handler handler = new Handler(controllerClass, method);
                                    //初始化action map
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * 获取handler
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
