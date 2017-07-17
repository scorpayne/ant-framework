package com.payne.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by dengpeng on 2017/6/20.
 */
public class ClassUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls = Class.forName(className,isInitialized,getClassLoader()); //Class.forName方法用来加载类 isInitialized为false，加载效率高
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 获取指定包名下的所有类
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                LOGGER.info("路径是："+url);
                if(url != null){
                    String protocol = url.getProtocol(); //获取此 URL 的协议名称
                    if("file".equals(protocol)){
                        String packagePath = url.getPath().replaceAll("%20", "");//去除文件路径中的空格
                        addClass(classSet,packagePath,packageName);
                    }else if("jar".equals(protocol)){
                        JarURLConnection jarURLConnection= (JarURLConnection)url.openConnection();
                        if(jarURLConnection!=null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while(jarEntries.hasMoreElements()){
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")){
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get class set failure",e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     *
     */
    public static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
//        E:\IntelliJ-workspace\chapter2
        File[] files= new File(packagePath).listFiles(new FileFilter() {
            //筛选出class文件及目录
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });
        for(File file:files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)){
                    className = packageName +"."+className;
                }
                doAddClass(classSet,className);
            }else{
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(packagePath)){
                    subPackagePath = packagePath+"/"+subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)){
                    subPackageName = packageName+"."+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }
}
