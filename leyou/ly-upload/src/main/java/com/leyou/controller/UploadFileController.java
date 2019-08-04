package com.leyou.controller;

import com.leyou.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadFileController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String url=uploadService.upload(file);
        if(StringUtils.isEmpty(url)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(url);
    }
}
