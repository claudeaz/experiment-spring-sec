package com.example.experiment1_spring_security.file;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile file,
            @Nonnull Integer userId
    ) {

        String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath
    ) {
        String finalFileUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalFileUploadPath);
        if (!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
            }
        }
        String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        //voici a quoi va ressembler l'URI: ./upload/users/1/223344443.png
        String targetFilePath = finalFileUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("File was not saved", e);
        }
        return  null;
    }

    private String getFileExtension(@Nullable String filename) {
        if (filename == null || filename.isEmpty()){
            return "";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1){
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
}
