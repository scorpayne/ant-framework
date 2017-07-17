package com.payne.framework.proxy;

import com.payne.framework.annotation.Transaction;
import com.payne.framework.helper.DatabaseHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by dengpeng on 2017/6/22.
 */
public class TransactionProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){

        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Boolean flag = FLAG_HOLDER.get();
        Method targetMethod = proxyChain.getTargetMethod();
        if(!flag && targetMethod.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try{
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }
        }else{
            //说明方法没有声明事务，直接执行代理
            result = proxyChain.doProxyChain();
        }
        return result;
    }

}
