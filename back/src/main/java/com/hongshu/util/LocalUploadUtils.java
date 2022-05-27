package com.hongshu.util;

import com.hongshu.exception.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 本地上传工具类
 *
 * @author Hong_Shu995
 * @date 2022-05-27
 */
@Component
public class LocalUploadUtils
{
    /**
     * 本地路径
     */
    @Value("${upload.path}")
    private String localPath;

    /**
     * 访问url
     */
    @Value("${upload.url}")
    private String localUrl;

    public String uploadFile(MultipartFile file)
    {
        try
        {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 判断文件是否已存在
            if (!exists(localPath + fileName))
            {
                // 不存在则继续上传
                upload(fileName, file.getInputStream());
            }
            // 返回文件访问路径
            return getFileAccessUrl(localPath + fileName);

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new MyException("文件上传失败");
        }
    }

    private Boolean exists(String filePath)
    {
        return new File(localPath + filePath).exists();
    }

    private String getFileAccessUrl(String filePath)
    {
        return localUrl + filePath;
    }

    private void upload(String fileName, InputStream inputStream) throws IOException
    {
        // 判断目录是否存在
        File directory = new File(localPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new MyException("创建目录失败");
            }
        }
        // 写入文件
        File file = new File(localPath + fileName);
        if (file.createNewFile()) {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[1024];
            int length;
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            bos.flush();
            inputStream.close();
            bis.close();
            bos.close();
        }
    }


}
