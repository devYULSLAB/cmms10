package com.cmms10.common.file.service;

import com.cmms10.common.file.entity.FileAttachment;
import com.cmms10.common.file.repository.FileAttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;

    @Value("${file.upload.root-path:uploads}")
    private String uploadRootPath;

    @Value("${file.upload.max-file-size:10MB}")
    private String maxFileSize;

    @Value("${file.upload.max-file-count:10}")
    private int maxFileCount;

    public FileAttachmentService(FileAttachmentRepository fileAttachmentRepository) {
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    /**
     * 파일 업로드 및 저장
     */
    public FileAttachment uploadFile(MultipartFile file, String fileGroupId, String companyId, String moduleType)
            throws IOException {
        // 파일 크기 검증
        validateFileSize(file);

        // 파일 개수 검증
        validateFileCount(fileGroupId);

        // 파일 ID 생성 (10자리, 밀리초 기반)
        String fileId = generateFileId();

        // 원본 파일명
        String originalFileName = file.getOriginalFilename();

        // 저장될 파일명 (UUID + 확장자)
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = UUID.randomUUID().toString() + fileExtension;

        // 파일 경로 생성
        String relativePath = String.format("%s/%s/%s", companyId, moduleType, fileGroupId);
        Path uploadPath = Paths.get(uploadRootPath, relativePath);

        // 디렉토리 생성
        Files.createDirectories(uploadPath);

        // 파일 저장
        Path filePath = uploadPath.resolve(storedFileName);
        file.transferTo(filePath.toFile());

        // 데이터베이스에 파일 정보 저장
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setId(fileId);
        fileAttachment.setFileGroupId(fileGroupId);
        fileAttachment.setOriginalFileName(originalFileName);
        fileAttachment.setStoredFileName(storedFileName);
        fileAttachment.setFilePath(relativePath + "/" + storedFileName);
        fileAttachment.setFileSize(file.getSize());
        fileAttachment.setContentType(file.getContentType());
        fileAttachment.setCompanyId(companyId);
        fileAttachment.setModuleType(moduleType);
        fileAttachment.setCreatedAt(LocalDateTime.now());

        return fileAttachmentRepository.save(fileAttachment);
    }

    /**
     * 파일 그룹 ID로 파일 목록 조회
     */
    public List<FileAttachment> getFilesByFileGroupId(String fileGroupId) {
        return fileAttachmentRepository.findByFileGroupId(fileGroupId);
    }

    /**
     * 회사 ID와 모듈 타입으로 파일 목록 조회
     */
    public List<FileAttachment> getFilesByCompanyIdAndModuleType(String companyId, String moduleType) {
        return fileAttachmentRepository.findByCompanyIdAndModuleType(companyId, moduleType);
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String fileId) {
        FileAttachment fileAttachment = fileAttachmentRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // 물리적 파일 삭제
        Path filePath = Paths.get(uploadRootPath, fileAttachment.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        // 데이터베이스에서 삭제
        fileAttachmentRepository.deleteById(fileId);
    }

    /**
     * 파일 그룹 ID로 모든 파일 삭제
     */
    public void deleteFilesByFileGroupId(String fileGroupId) {
        List<FileAttachment> files = fileAttachmentRepository.findByFileGroupId(fileGroupId);

        for (FileAttachment file : files) {
            // 물리적 파일 삭제
            Path filePath = Paths.get(uploadRootPath, file.getFilePath());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 로그만 남기고 계속 진행
                System.err.println("Failed to delete file: " + filePath);
            }
        }

        // 데이터베이스에서 삭제
        fileAttachmentRepository.deleteByFileGroupId(fileGroupId);
    }

    /**
     * 파일 ID 생성 (10자리, 밀리초 기반)
     */
    private String generateFileId() {
        long currentTimeMillis = System.currentTimeMillis();
        return String.format("%010d", currentTimeMillis % 10000000000L);
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 파일 다운로드 경로 생성
     */
    public String getDownloadPath(String fileId) {
        FileAttachment fileAttachment = fileAttachmentRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return "/file/download/" + fileId;
    }

    /**
     * 파일 ID로 파일 조회
     */
    public FileAttachment getFileById(String fileId) {
        return fileAttachmentRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    /**
     * 파일 크기 검증
     */
    private void validateFileSize(MultipartFile file) {
        long maxSizeBytes = parseFileSize(maxFileSize);
        if (file.getSize() > maxSizeBytes) {
            throw new RuntimeException("File size exceeds maximum allowed size: " + maxFileSize);
        }
    }

    /**
     * 파일 개수 검증
     */
    private void validateFileCount(String fileGroupId) {
        List<FileAttachment> existingFiles = fileAttachmentRepository.findByFileGroupId(fileGroupId);
        if (existingFiles.size() >= maxFileCount) {
            throw new RuntimeException("Maximum file count exceeded: " + maxFileCount);
        }
    }

    /**
     * 파일 크기 문자열을 바이트로 변환
     */
    private long parseFileSize(String sizeStr) {
        sizeStr = sizeStr.toUpperCase();
        if (sizeStr.endsWith("KB")) {
            return Long.parseLong(sizeStr.substring(0, sizeStr.length() - 2)) * 1024;
        } else if (sizeStr.endsWith("MB")) {
            return Long.parseLong(sizeStr.substring(0, sizeStr.length() - 2)) * 1024 * 1024;
        } else if (sizeStr.endsWith("GB")) {
            return Long.parseLong(sizeStr.substring(0, sizeStr.length() - 2)) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(sizeStr);
        }
    }

    /**
     * 파일 그룹 ID 생성 (10자리, 밀리초 기반)
     * 다른 모듈에서도 사용할 수 있도록 public으로 제공
     */
    public String generateFileGroupId() {
        long currentTimeMillis = System.currentTimeMillis();
        return String.format("%010d", currentTimeMillis % 10000000000L);
    }
}
