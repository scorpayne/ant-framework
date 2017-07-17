package com.payne.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dengpeng on 2017/6/22.
 */
public class StreamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);
    /**
     * 将输入流复制到输出流
     */
    public static void copyStream(InputStream inputStream,OutputStream outputStream){
        int length;
        byte[] buffer = new byte[4*1024];
        try {
            while((length = inputStream.read(buffer, 0, buffer.length))!=-1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("copy stream failure",e);
            throw new  RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
