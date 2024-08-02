package com.example.posapplicationapis.restcontroller.Image;


import com.example.posapplicationapis.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestImageUpload {

    @PostMapping("/add-image")
    public void addMedia(@RequestParam("file") MultipartFile file) throws IOException {
        ImageService imageService = new ImageService();
        String link = imageService.uploadFile(file);

        System.out.printf(link);
    }
}
