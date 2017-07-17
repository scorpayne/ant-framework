package com.payne.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by dengpeng on 2017/6/21.
 */
public class Request {
    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求路径
     */
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public boolean equals(Object o) {
/*        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (!requestMethod.equals(request.requestMethod)) return false;
        return requestPath.equals(request.requestPath);*/
        return EqualsBuilder.reflectionEquals(this,o);
    }

    @Override
    public int hashCode() {
/*        int result = requestMethod.hashCode();
        result = 31 * result + requestPath.hashCode();
        return result;*/
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
