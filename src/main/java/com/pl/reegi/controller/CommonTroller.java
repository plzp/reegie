package com.pl.reegi.controller;

import com.pl.reegi.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonTroller {
    @Value("${reegie.fileName}")
    private String baseFileName;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();

        String FileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        File file1 = new File(baseFileName);
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            file.transferTo(new File(baseFileName+FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(FileName);
    }

    /**
     * 文件下载
     * @param name 文件名称
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        //输入流，读取文件、
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(baseFileName + name));
            log.info("文件路径为：  "+baseFileName + name);
            //输出流，将文件写入浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];

            while((len=fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
