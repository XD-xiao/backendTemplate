package com.example.backendtemplate.Service;

import com.example.backendtemplate.Utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileUtil fileUtil;

    /**
     * 保存多个文件，返回可访问的 URL 列表
     */
    public List<String> saveFile(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String url = fileUtil.saveFile(file);
            urls.add(url);
        }
        return urls;
    }

    /**
     * 根据 fileId 加载文件资源（供 Controller 返回）
     * @param fileId 文件 ID（如 "6f83...1c71.png"）
     * @return Resource 或 null（若文件不存在）
     */
    public Resource getFileResource(String fileId) {
        // 校验 fileId 合法性（防止路径穿越）
        if (!fileUtil.isValidFileId(fileId)) {
            return null;
        }

        // 权限控制
        //无权限返回return null;


        Path filePath = fileUtil.resolveFilePath(fileId);
        if (filePath == null || !filePath.toFile().exists()) {
            return null;
        }

        return fileUtil.loadAsResource(filePath);
    }

    /**
     * 检测文件 MIME 类型（供 Controller 设置 Content-Type）
     */
    public String detectContentType(Path filePath) {
        return fileUtil.detectContentType(filePath);
    }
}