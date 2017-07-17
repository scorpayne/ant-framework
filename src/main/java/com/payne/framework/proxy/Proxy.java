package com.payne.framework.proxy;

/**
 * Created by dengpeng on 2017/6/21.
 */
public interface Proxy {
    /**
     * 执行链式代理,可以将多个代理通过一条链子串起来，一个个去执行
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
