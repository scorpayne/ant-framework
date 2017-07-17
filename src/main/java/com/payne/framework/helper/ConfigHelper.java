package com.payne.framework.helper;

import com.payne.framework.ConfigConstant;
import com.payne.framework.util.PropsUtil;

import java.util.Map;
import java.util.Properties;

/**
 * Created by dengpeng on 2017/6/20.
 */
public class ConfigHelper {
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取jdbc驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }

    public static String getAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");
    }

    /**
     * 获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS, key);
    }

    /**
     * 获取 String 类型的属性值（可指定默认值）
     */
    public static String getString(String key, String defaultValue) {
        return PropsUtil.getString(CONFIG_PROPS, key, defaultValue);
    }

    /**
     * 获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropsUtil.getNumber(CONFIG_PROPS, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static int getInt(String key, int defaultValue) {
        return PropsUtil.getNumber(CONFIG_PROPS, key, defaultValue);
    }

    /**
     * 获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key, defaultValue);
    }

    /**
     * 获取指定前缀的相关属性
     *
     * @since 2.2
     */
    public static Map<String, Object> getMap(String prefix) {
        return PropsUtil.getMap(CONFIG_PROPS, prefix);
    }

    /**
     * 获取应用文件上传限制
     */
    public static int getAppUploadLimit(){
        return PropsUtil.getNumber(CONFIG_PROPS,ConfigConstant.APP_UPLOAD_LIMIT,10);
    }
}
