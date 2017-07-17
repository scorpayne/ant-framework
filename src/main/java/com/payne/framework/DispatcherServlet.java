package com.payne.framework;

import com.payne.framework.bean.Data;
import com.payne.framework.bean.Handler;
import com.payne.framework.bean.Param;
import com.payne.framework.bean.View;
import com.payne.framework.helper.*;
import com.payne.framework.util.*;
import sun.security.krb5.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dengpeng on 2017/6/21.
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化HelpLoader类
        HelpLoader.init();
        //获取servletContext对象，用于注册特定的servlet
        ServletContext servletContext = config.getServletContext();
        //注册处理JSP的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        Set<String> strings = jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

        // 5.0
        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletHelper.init(req,resp);
        try {
            //获取请求方法和请求路径
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();

            //5.0  favicon.ico表示缓存文件
            if ("/favion.ico".equals(requestPath)) {
                return;
            }

            //获取action处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                //获取controller类及bean实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);

                //这里对Param进行了重构
/*            //创建请求参数集合
            Map<String,Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();//获取请求参数的key
                String paramValue = req.getParameter(paramName);//请求参数的值
                paramMap.put(paramName,paramValue);
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params = StringUtil.splitString(body, "&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param:params){
                        String[] array = StringUtil.splitString(param, "=");
                        if(ArrayUtil.isNotEmpty(array) && array.length == 2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            */
                //5.0
                Param param;
                if (UploadHelper.isMultipart(req)) {
                    param = UploadHelper.createParam(req);
                } else {
                    param = RequestHelper.createParam(req);
                }

                Object result;
                //调用action方法
                Method actionMethod = handler.getActionMethod();

                if (param.isEmpty()) {
                    result = ReflectionUtils.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtils.invokeMethod(controllerBean, actionMethod, param);
                }

                //处理action方法返回值
                if (result instanceof View) {
                    //表示返回JSP页面
                    View view = (View) result;
                    handleViewResult(view, req, resp);
                } else if (result instanceof Data) {
                    Data data = (Data) result;
                    handleDataResult(data, resp);
                }
            }
        }finally {
            ServletHelper.destroy();
        }
    }

    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
        String path = view.getPath();
        if(StringUtil.isNotEmpty(path)){
            if(path.startsWith("/")){
                response.sendRedirect(request.getContextPath()+path);//重定向页面，页面会跳转
            }else{
                Map<String, Object> model = view.getModel();
                for(Map.Entry<String,Object> entry:model.entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
                //只是刷新数据
                request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
            }
        }
    }

    private void handleDataResult(Data data,HttpServletResponse response)throws IOException{
        Object model = data.getModel();
        if(model != null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJSON(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
