package com.minio.upload.controller;

import com.minio.upload.service.MinioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: ray
 * @Date: 2022/4/6 14:12
 **/
@RestController
public class UploadController {

    //上传图片到minio服务器
    @PostMapping("/pic")
    public String uploadFileUpload(MultipartFile file) throws Exception {
        return MinioService.uploadInputStream("ray", "ray1", file.getInputStream());
    }


    //下载指定的图片
    @GetMapping("/pic")
    public void downloadPic(HttpServletResponse response) throws Exception {
        InputStream stream = MinioService.download("ray", "ray1");
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024 * 4];

        int n = 0;
        while (-1 != (n = stream.read(buffer))) {
            outputStream.write(buffer, 0, n);
        }
        //将buffer流写到前端
        response.setContentType("image/jpeg");
        outputStream.write(buffer);
        outputStream.flush();
        outputStream.close();

    }


    //获取有效时长的图片链接地址
    @GetMapping("/expire")
    public String readFilePicInExpire() throws Exception {
        return MinioService.getSignedUrl("ray", "ray1", 10);
    }

}
