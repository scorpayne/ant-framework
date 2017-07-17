package com.payne.framework.bean;

import com.payne.framework.util.CastUtil;
import com.payne.framework.util.CollectionUtil;
import com.payne.framework.util.MapUtil;
import com.payne.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengpeng on 2017/6/21.
 */
public class Param {
    private Map<String,Object> paramMap;

    //5.0重构参数，表单中，分一般表单参数和文件参数
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取所有字段信息
     * 5.0获取普通表单的map映射
     */
    public Map<String,Object> getFieldMap(){
        //5.0
        Map<String,Object> fieldMap = new HashMap<>();

        if(CollectionUtil.isNotEmpty(formParamList)){
            for(FormParam formParam:formParamList){
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if(fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 5.0 获取上传文件映射
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<>();

        if(CollectionUtil.isNotEmpty(fileParamList)){
            for(FileParam fileParam:fileParamList){
                String fieldName = fileParam.getFieldName();

                List<FileParam> fileParamList;
                if(fileMap.containsKey(fieldName)){
                    fileParamList = fileMap.get(fieldName);
                }else{
                    fileParamList = new ArrayList<>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     */
    public FileParam getFile(String fieldName){
        List<FileParam> fileList = getFileList(fieldName);
        if(CollectionUtil.isNotEmpty(fileList) && fileList.size()==1){
            return fileList.get(0);
        }
        return null;
    }

    /**
     * 根据参数名获取long型参数
     * @param name
     * @return
     */
/*    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }*/

    /**
     * 验证参数是否为空
     */
    public boolean isEmpty(){
//        return MapUtil.isEmpty(paramMap);
        //5.0
        return CollectionUtil.isEmpty(fileParamList) && CollectionUtil.isEmpty(formParamList);
    }

    /**
     * 根据参数名获取String型参数
     */
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 获取double型参数值
     */
    public double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 获取long型参数值
     */
    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 获取int型参数值
     */
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }
    /**
     * 获取boolean型参数值
     */
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFieldMap().get(name));
    }
}
