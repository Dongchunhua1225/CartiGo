package com.powernode.controller;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.powernode.config.AliyunOSSConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;

/**
 * 文件上传控制层
 */
@Api(tags = "文件上传接口管理")
@RequestMapping("admin/file")
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    @Autowired
    private AliyunOSSConfig aliyunOSSConfig;

    /**
     * 上传文件：<br>
     * 1.接口要求请求方式必须是post请求<br>
     * 2.接收文件的对象的类型是：MultipartFile该对象是SpringMVC提供的
     */
    @ApiOperation("上传单个文件")
    @PostMapping("upload/element")
    public String uploadFile(MultipartFile file) {
        //String endpoint = aliyunOSSConfig.getEndpoint();

        // 填写Bucket名称
        String bucketName = aliyunOSSConfig.getBucketName();

        // 以天为单位的名称，作为文件夹名称
        String newFolderName = DateUtil.format(new Date(), "yyyy-MM-dd");
        // 以时间戳作为文件的新名称
        String newFileName = DateUtil.format(new Date(), "HHmmssSSS");
        // 获取原文件的后缀名称
        String originalFilename = file.getOriginalFilename();
        //一个文件名字可以有很多个“.” 但是我只取其中最后一个“.”后面的 --> 这才是后缀
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 填写Object完整路径，完整路径中不能包含Bucket名称
        String objectName = newFolderName + "/" + newFileName + fileSuffix;

        OSS ossClient = new OSSClientBuilder().build(aliyunOSSConfig.getEndpoint(),
                aliyunOSSConfig.getAccessKeyId(), aliyunOSSConfig.getAccessKeySecret());

        //把声明放在外边
        URL url = null;
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file.getInputStream());
            // 上传
            ossClient.putObject(putObjectRequest);
            // 创建上传文件访问的url地址
            url = ossClient.generatePresignedUrl(bucketName, objectName, DateUtil.offsetDay(new Date(), 365*10));
            System.out.println("url: " + url);
        } catch (Exception ce) {
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url.toString();
    }


}
