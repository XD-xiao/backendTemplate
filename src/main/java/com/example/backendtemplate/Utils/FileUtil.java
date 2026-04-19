package com.example.backendtemplate.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class FileUtil {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.port}")
    private int port;

    /**
     * 保存上传文件，使用内容哈希作为文件名，避免重复。
     */
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空");
        }

        String baseUrl = "http://localhost:" + port + "/file/";

        byte[] fileBytes = file.getBytes();
        String fileHash = computeSha256(fileBytes);
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);

        String newFileName = (extension != null && !extension.isEmpty())
                ? fileHash + "." + extension
                : fileHash;

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(newFileName).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IOException("非法文件路径");
        }

        if (!Files.exists(filePath)) {
            Files.write(filePath, fileBytes);
        } else {
            System.out.println("文件已存在，跳过写入: " + newFileName);
        }

        return baseUrl + newFileName;
    }

    /**
     * 校验 fileId 是否合法（仅允许 SHA256 + 可选扩展名）
     * 示例合法值: "a1b2...c3.png", "a1b2...c3"
     */
    public boolean isValidFileId(String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            return false;
        }
        // 允许格式: [64位十六进制].[任意字母数字] 或 纯64位十六进制
        return fileId.matches("^[a-fA-F0-9]{64}(\\.[a-zA-Z0-9]+)?$");
    }

    /**
     * 根据 fileId 解析出本地文件路径（安全）
     */
    public Path resolveFilePath(String fileId) {
        if (!isValidFileId(fileId)) {
            return null;
        }
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(fileId).normalize();

        // 再次校验路径是否在 uploadDir 内（防御性编程）
        if (!filePath.startsWith(uploadPath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 将文件路径转为 Spring Resource
     */
    public Resource loadAsResource(Path filePath) {
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 探测文件的 MIME 类型
     */
    public String detectContentType(Path filePath) {
        try {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                return "application/octet-stream";
            }
            return contentType;
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private String computeSha256(byte[] data) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            return HexFormat.of().formatHex(hashBytes); // Java 17+
        } catch (Exception e) {
            throw new IOException("计算文件哈希失败", e);
        }
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.lastIndexOf('.') == -1) {
            return null;
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}