package com.user_manager_v1.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageController {

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Define the maximum file size in bytes (e.g. 10 MB)
            long maxFileSize = 20 * 1024 * 1024;

            // Check if the file size exceeds the maximum
            if (file.getSize() > maxFileSize) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The file size exceeds the maximum allowed size of " + maxFileSize + " bytes.");
            }

            // Save the uploaded file to disk
            String fileName = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(fileName);
            File newFile = File.createTempFile("image-", "." + fileExtension);
            file.transferTo(newFile);

            // Return the URL of the saved file
            String fileUrl = "http://localhost:8090/images/" + newFile.getName();
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
