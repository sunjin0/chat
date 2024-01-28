package com.example.chatuser.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.chatuser.service.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class UploadServiceImpl {
    //通过工具类来获取相应的值
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;


    /**
     * 上传
     *
     * @param file         文件
     * @param fileTypeName 文件类型名称 有image，voice，video
     * @return {@link String}
     */
    public String upload(MultipartFile file, String fileTypeName) {

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String filename = file.getOriginalFilename();

            //1、在文件名称里添加一个随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            //2、把文件按照日期进行分类
            // 2021/7/17/xx.jpg
            //获取当前的日期
            String datePath = new DateFormatUtil("yyyy/MM/dd").get();
            filename = fileTypeName + "/" + datePath + "/" + filename;

            //调用OSS方法实现上传
            //第一个参数 Bucket名称
            //第二个参数  上传到OSS文件路径和文件名称
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, filename, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            return "https://" + bucketName + "." + endpoint + "/" + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
