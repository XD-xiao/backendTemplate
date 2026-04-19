package com.example.backendtemplate.Controller;

import com.example.backendtemplate.Pojo.Result;
import com.example.backendtemplate.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    // ========== 文件上传 ==========
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return Result.error(400,"没有文件被上传");
        }
        List<String> fileUrls = fileService.saveFile(files);
        if (fileUrls == null || fileUrls.isEmpty()) {
            return Result.error(400,"上传失败");
        }
        return Result.success(fileUrls);
    }

    // ========== 文件下载/访问 ==========
    /**
     * 根据 fileId 获取文件
     * URL 示例: /file/6f83dcc8283b94ec0bb31323b3eea333e906a08097ce26b2cb5bf3957f661c71.png
     */
    @GetMapping("/file/{fileId:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileId) {
        try {
            Resource resource = fileService.getFileResource(fileId);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            // 推测 MIME 类型（可选）
            String contentType = fileService.detectContentType(resource.getFile().toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileId + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}