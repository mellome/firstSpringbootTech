package com.yifeng.springboot.sprintbootdemo.controller;

import com.yifeng.springboot.sprintbootdemo.common.Constants;
import com.yifeng.springboot.sprintbootdemo.common.Result;
import com.yifeng.springboot.sprintbootdemo.common.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

    @Controller
    @RequestMapping("/images")
    public class UploadController {

        @RequestMapping(value = "/upload", method = RequestMethod.POST)
        @ResponseBody
        public Result upload(@RequestParam("file") MultipartFile file) {
            // @RequestParam("file1") 用来获取前端上传请求name为file1的文件, 然后复制给当前parameter "file"
            if (file.isEmpty()) {
                return ResultGenerator.genFailResult("please choose a doc");
            }
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            //生成文件名称通用方法
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();
            try {
                // 保存文件
                byte[] bytes = file.getBytes();
                Path path = Paths.get(Constants.FILE_UPLOAD_PATH + newFileName);
                Files.write(path, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Result result = ResultGenerator.genSuccessResult();
            result.setData("files/" + newFileName);
            return result;
        }
}
