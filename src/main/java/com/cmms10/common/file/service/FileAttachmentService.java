package com.cmms10.common.file.service;

import com.cmms10.common.file.entity.FileAttachment;
import com.cmms10.common.file.repository.FileAttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;

    @Value("${file.upload.root-path:uploads}")
    private String uploadRootPath;

    @Value("${file.upload.max-file-size:10MB}")
    private String maxFileSize;

    @Value("${file.upload.max-file-count:10}")
    private int maxFileCount;

    /**
     * 파일 업로드 및 저장
     */
    public FileAttachment uploadFile(String companyId,
            String username,
            String moduleName,
            MultipartFile file,
            String fileGroupId)
            throws IOException {
        // 파일 크기 검증
        validateFileSize(file);

        // 파일 개수 검증
        validateFileCount(companyId, fileGroupId);

        // 파일 ID 생성 (10자리, 밀리초 기반)
        String fileId = generateFileId();

        // 원본 파일명
        String originalFileName = file.getOriginalFilename();

        // 저장될 파일명 (UUID + 확장자)
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = UUID.randomUUID().toString() + fileExtension;

        // 파일 경로 생성 - 절대 경로로 처리
        String relativePath = String.format("%s/%s/%s", companyId, moduleName, fileGroupId);
        Path uploadPath = getAbsoluteUploadPath(relativePath);

        // 디렉토리 생성 (부모 디렉토리까지 모두 생성)
        try {
            Files.createDirectories(uploadPath);
            System.out.println("Created directory: " + uploadPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + uploadPath.toAbsolutePath());
            throw new RuntimeException("Failed to create upload directory: " + e.getMessage(), e);
        }

        // 파일 저장
        Path filePath = uploadPath.resolve(storedFileName);
        try {
            file.transferTo(filePath.toFile());
            System.out.println("File saved: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save file: " + filePath.toAbsolutePath());
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }

        // 데이터베이스에 파일 정보 저장
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setCompanyId(companyId);
        fileAttachment.setFileId(fileId);
        fileAttachment.setFileGroupId(fileGroupId);
        fileAttachment.setOriginalFileName(originalFileName);
        fileAttachment.setStoredFileName(storedFileName);
        fileAttachment.setFilePath(relativePath + "/" + storedFileName);
        fileAttachment.setFileSize(file.getSize());
        fileAttachment.setContentType(file.getContentType());
        fileAttachment.setModuleName(moduleName);
        fileAttachment.setCreateDate(LocalDateTime.now());
        fileAttachment.setCreatedBy(username);

        return fileAttachmentRepository.save(fileAttachment);
    }

    /**
     * 파일 그룹 ID로 파일 목록 조회
     */
    public List<FileAttachment> getFilesByCompanyIdAndFileGroupId(String companyId, String fileGroupId) {
        return fileAttachmentRepository.findByCompanyIdAndFileGroupId(companyId, fileGroupId);
    }

    /**
     * 회사 ID와 모듈명으로 파일 목록 조회
     */
    public List<FileAttachment> getFilesByCompanyIdAndModuleName(String companyId, String moduleName) {
        return fileAttachmentRepository.findByCompanyIdAndModuleName(companyId, moduleName);
    }

    /**
     * 파일 삭제
     */
    public void deleteByCompanyIdAndFileId(String companyId, String fileId) {
        FileAttachment fileAttachment = fileAttachmentRepository.findByCompanyIdAndFileId(companyId, fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // 물리적 파일 삭제 - 절대 경로 사용
        String userDir = System.getProperty("user.dir");
        Path filePath = Paths.get(userDir, uploadRootPath, fileAttachment.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            System.out.println("Deleted file: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        // 데이터베이스에서 삭제
        fileAttachmentRepository.deleteByCompanyIdAndFileId(companyId, fileId);
    }

    /**
     * 파일 그룹 ID로 모든 파일 삭제
     */
    public void deleteFilesByCompanyIdAndFileGroupId(String companyId, String fileGroupId) {
        List<FileAttachment> files = fileAttachmentRepository.findByCompanyIdAndFileGroupId(companyId, fileGroupId);

        for (FileAttachment file : files) {
            // 물리적 파일 삭제 - 절대 경로 사용
            String userDir = System.getProperty("user.dir");
            Path filePath = Paths.get(userDir, uploadRootPath, file.getFilePath());
            try {
                Files.deleteIfExists(filePath);
                System.out.println("Deleted file: " + filePath.toAbsolutePath());
            } catch (IOException e) {
                // 로그만 남기고 계속 진행
                System.err.println("Failed to delete file: " + filePath);
            }
        }

        // 데이터베이스에서 삭제
        fileAttachmentRepository.deleteByCompanyIdAndFileGroupId(companyId, fileGroupId);
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
    public String getDownloadPath(String companyId, String fileId) {
        FileAttachment fileAttachment = fileAttachmentRepository.findByCompanyIdAndFileId(companyId, fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return "/file/download/" + fileId;
    }

    /**
     * 파일 ID로 파일 조회
     */
    public FileAttachment getFileById(String fileId) {
        // 복합 PK를 사용하므로 companyId가 필요하지만, 기존 호환성을 위해 임시로 처리
        // 실제로는 companyId도 함께 전달받아야 함
        throw new RuntimeException("getFileById method requires companyId. Use getFileByCompanyIdAndFileId instead.");
    }

    /**
     * 회사 ID와 파일 ID로 파일 조회
     */
    public FileAttachment getFileByCompanyIdAndFileId(String companyId, String fileId) {
        return fileAttachmentRepository.findByCompanyIdAndFileId(companyId, fileId)
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
    private void validateFileCount(String companyId, String fileGroupId) {
        List<FileAttachment> existingFiles = fileAttachmentRepository.findByCompanyIdAndFileGroupId(companyId,
                fileGroupId);
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
     * 파일 그룹 ID 생성 (FG + 8자리, 밀리초 기반)
     * 다른 모듈에서도 사용할 수 있도록 public으로 제공
     */
    public String generateFileGroupId() {
        long currentTimeMillis = System.currentTimeMillis();
        return "FG" + String.format("%08d", currentTimeMillis % 100000000L);
    }

    /**
     * 파일 ID 생성 (FI + 8자리, 밀리초 기반 + 랜덤)
     */
    private String generateFileId() {
        long currentTimeMillis = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "FI" + String.format("%05d", currentTimeMillis % 100000L) + String.format("%03d", random);
    }

    /**
     * 절대 업로드 경로 생성
     */
    private Path getAbsoluteUploadPath(String relativePath) {
        // 애플리케이션 루트 디렉토리를 기준으로 절대 경로 생성
        String userDir = System.getProperty("user.dir");
        Path rootPath = Paths.get(userDir, uploadRootPath);
        Path fullPath = rootPath.resolve(relativePath);

        System.out.println("User directory: " + userDir);
        System.out.println("Root upload path: " + rootPath.toAbsolutePath());
        System.out.println("Full upload path: " + fullPath.toAbsolutePath());

        return fullPath;
    }

}
