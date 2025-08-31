package com.cmms10.common.file.controller;

import com.cmms10.common.file.entity.FileAttachment;
import com.cmms10.common.file.service.FileAttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileAttachmentController {

    private final FileAttachmentService fileAttachmentService;

    @Value("${file.upload.root-path:uploads}")
    private String uploadRootPath;

    public FileAttachmentController(FileAttachmentService fileAttachmentService) {
        this.fileAttachmentService = fileAttachmentService;
    }

    /**
     * 파일 업로드
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam("fileGroupId") String fileGroupId,
                                       @RequestParam("moduleType") String moduleType,
                                       HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");
            if (companyId == null) {
                return ResponseEntity.badRequest().body("Company ID not found in session");
            }

            FileAttachment fileAttachment = fileAttachmentService.uploadFile(file, fileGroupId, companyId, moduleType);
            return ResponseEntity.ok(fileAttachment);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * 파일 그룹 ID로 파일 목록 조회
     */
    @GetMapping("/list/{fileGroupId}")
    @ResponseBody
    public ResponseEntity<List<FileAttachment>> getFilesByFileGroupId(@PathVariable String fileGroupId) {
        List<FileAttachment> files = fileAttachmentService.getFilesByFileGroupId(fileGroupId);
        return ResponseEntity.ok(files);
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        try {
            FileAttachment fileAttachment = fileAttachmentService.getFileById(fileId);

            Path filePath = Paths.get(uploadRootPath, fileAttachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + fileAttachment.getOriginalFileName() + "\"")
                        .contentType(MediaType.parseMediaType(fileAttachment.getContentType()))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/delete/{fileId}")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String fileId) {
        try {
            fileAttachmentService.deleteFile(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

    /**
     * 파일 그룹 ID로 모든 파일 삭제
     */
    @DeleteMapping("/delete-group/{fileGroupId}")
    @ResponseBody
    public ResponseEntity<?> deleteFilesByFileGroupId(@PathVariable String fileGroupId) {
        try {
            fileAttachmentService.deleteFilesByFileGroupId(fileGroupId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete files: " + e.getMessage());
        }
    }
}
