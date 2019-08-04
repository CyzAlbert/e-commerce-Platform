package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    private static List<String> suffixes= Arrays.asList("image/png","image/jpeg");

    private static Logger logger= LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String upload(MultipartFile file) {

        try {
            String type=file.getContentType();
            if(!suffixes.contains(type)){
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }

            //获取图片文件扩展名
            String extention = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            //文件上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extention, null);
            //获取上传的文件路径
            String url = "http://image.leyou.com/" + storePath.getFullPath();

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
