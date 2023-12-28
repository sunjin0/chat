package com.example.chatuser.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.chatuser.util.ConstantPropertiesUtils;
import com.example.chatuser.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class UploadServiceImpl {
    public String upload(MultipartFile file) {
        //通过工具类来获取相应的值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

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
            filename = datePath + "/" + filename;

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
