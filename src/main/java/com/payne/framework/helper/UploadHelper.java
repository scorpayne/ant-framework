package com.payne.framework.helper;

import com.payne.framework.bean.FileParam;
import com.payne.framework.bean.FormParam;
import com.payne.framework.bean.Param;
import com.payne.framework.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dengpeng on 2017/6/22.
 */
public final class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * apache commons fileUpload提供的Servlet文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext){
        File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));

        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if(uploadLimit!=0){
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    /**
     * 判断请求是否为multipart类型，当上传文件时，请求才会是multipart类型
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求对象
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        List<FileParam> fileParamList = new ArrayList<>();
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if(MapUtil.isNotEmpty(fileItemListMap)){
                for(Map.Entry<String,List<FileItem>> fileItemListEntry: fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if(CollectionUtil.isNotEmpty(fileItemList)){
                        for(FileItem fileItem:fileItemList){
                            if(fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else{
                                //去掉文件路径的真实文件名
                                String realFileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                                if(StringUtil.isNotEmpty(realFileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParamList.add(new FileParam(fieldName,realFileName,fileSize,contentType,inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param failure",e);
            throw new RuntimeException();
        }
        return new Param(formParamList,fileParamList);
    }

    /**
     * 上传文件,
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        try {
            if (fileParam != null) {
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
                StreamUtil.copyStream(inputStream,outputStream);
            }
        }catch (Exception e){
            LOGGER.error("upload file failure,file name is:"+fileParam.getFileName(),e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     */
    public static void uploadFile(String basePath,List<FileParam> fileParamList){
        try{
            if(CollectionUtil.isNotEmpty(fileParamList)){
                for(FileParam fileParam:fileParamList){
                    uploadFile(basePath,fileParam);
                }
            }
        }catch (Exception e){
            LOGGER.error("upload mult file failure",e);
            throw new RuntimeException(e);
        }
    }
}
