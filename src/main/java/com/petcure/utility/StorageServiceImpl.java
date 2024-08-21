package com.petcure.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageServiceImpl implements StorageService {

    @Value("${disk.upload.basepath}")
    private String BASEPATH;

    @Override
    public List<String> loadAll() {
        File dirPath = new File(BASEPATH);
        return Arrays.asList(dirPath.list());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            // Print the BASEPATH to check if it's correct
            System.out.println("Base Path: " + BASEPATH);

            // Print the original filename
            System.out.println("Original Filename: " + file.getOriginalFilename());

            // Extract the file extension
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            System.out.println("File Extension: " + ext);

            // Generate a new unique filename
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;

            // Create the directory if it doesn't exist
            File directory = new File(BASEPATH);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory structure
            }

            // Construct the file path
            Path filePath = Paths.get(BASEPATH).resolve(fileName);

            // Store the file
            try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                FileCopyUtils.copy(file.getInputStream(), out);
                return fileName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource load(String fileName) {
        File filePath = new File(BASEPATH, fileName);
        if (filePath.exists()) {
            return new FileSystemResource(filePath);
        }
        return null;
    }

    @Override
    public void delete(String fileName) {
        File filePath = new File(BASEPATH, fileName);
        if (filePath.exists()) {
            filePath.delete();
        }
    }
}
