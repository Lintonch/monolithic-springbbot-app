package com.ffma.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
 
    private final Path root = Paths.get("uploads");
 
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }
 
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }
 
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename).normalize();
            System.out.println("Looking for file at: " + file.toAbsolutePath());

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed file path: " + e.getMessage(), e);
        }
    }

}