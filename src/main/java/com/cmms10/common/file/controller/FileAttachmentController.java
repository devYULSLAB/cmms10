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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam("moduleName") String moduleName,
            HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");

            // 상세 로깅 추가
            System.out.println("=== File Upload Request ===");
            System.out.println("Company ID: " + companyId);
            System.out.println("File Group ID: " + fileGroupId);
            System.out.println("Module Name: " + moduleName);
            System.out.println("File Name: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("File Size: " + (file != null ? file.getSize() : "null"));
            System.out.println("File Empty: " + (file != null ? file.isEmpty() : "null"));
            System.out.println("==========================");

            if (companyId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Company ID not found in session");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 파일이 비어있는지 확인
            if (file == null || file.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "File is empty or null");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // fileGroupId 검증
            if (fileGroupId == null || fileGroupId.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "File Group ID is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 파일 정보 로깅
            System.out.println("Uploading file: " + file.getOriginalFilename() +
                    ", size: " + file.getSize() +
                    ", companyId: " + companyId +
                    ", moduleName: " + moduleName +
                    ", fileGroupId: " + fileGroupId);

            FileAttachment fileAttachment = fileAttachmentService.uploadFile(companyId, moduleName, file, fileGroupId);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("fileData", fileAttachment);

            System.out.println("File upload successful: " + fileAttachment.getFileId());
            return ResponseEntity.ok(successResponse);
        } catch (IOException e) {
            System.err.println("IOException during file upload: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            System.err.println("RuntimeException during file upload: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Upload failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error during file upload: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 파일 그룹 ID로 파일 목록 조회
     */
    @GetMapping("/list/{fileGroupId}")
    @ResponseBody
    public ResponseEntity<List<FileAttachment>> getFilesByFileGroupId(@PathVariable String fileGroupId,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        if (companyId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<FileAttachment> files = fileAttachmentService.getFilesByCompanyIdAndFileGroupId(companyId, fileGroupId);
        return ResponseEntity.ok(files);
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");
            if (companyId == null) {
                return ResponseEntity.badRequest().build();
            }

            FileAttachment fileAttachment = fileAttachmentService.getFileByCompanyIdAndFileId(companyId, fileId);

            // 절대 경로로 파일 경로 생성
            String userDir = System.getProperty("user.dir");
            Path filePath = Paths.get(userDir, uploadRootPath, fileAttachment.getFilePath());
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
    public ResponseEntity<?> deleteFile(@PathVariable String fileId, HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");
            if (companyId == null) {
                return ResponseEntity.badRequest().body("Company ID not found in session");
            }

            fileAttachmentService.deleteByCompanyIdAndFileId(companyId, fileId);
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
    public ResponseEntity<?> deleteFilesByFileGroupId(@PathVariable String fileGroupId, HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");
            if (companyId == null) {
                return ResponseEntity.badRequest().body("Company ID not found in session");
            }

            fileAttachmentService.deleteFilesByCompanyIdAndFileGroupId(companyId, fileGroupId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete files: " + e.getMessage());
        }
    }
}
