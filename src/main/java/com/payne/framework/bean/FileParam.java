package com.payne.framework.bean;

import java.io.InputStream;

/**
 * Created by dengpeng on 2017/6/22.
 */
public class FileParam {
    private String fieldName; //文件表单的字段名
    private String fileName;//文件名
    private long fileSize;//文件大小
    private String contentType;//文件类型，doc,jpg
    private InputStream inputStream;//字节输入流

    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
