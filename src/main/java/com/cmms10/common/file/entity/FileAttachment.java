package com.cmms10.common.file.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * cmms10 - FileAttachment
 * 파일 첨부 정보를 관리하는 엔티티
 */
@Entity
@Table(name = "fileattachment")
public class FileAttachment {

    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "filegroupid", length = 10, nullable = false)
    private String fileGroupId;

    @Column(name = "originalfilename", length = 255, nullable = false)
    private String originalFileName;

    @Column(name = "storedfilename", length = 255, nullable = false)
    private String storedFileName;

    @Column(name = "filepath", length = 500, nullable = false)
    private String filePath;

    @Column(name = "filesize")
    private Long fileSize;

    @Column(name = "contenttype", length = 100)
    private String contentType;

    @Column(name = "companyid", length = 10, nullable = false)
    private String companyId;

    @Column(name = "moduletype", length = 50, nullable = false)
    private String moduleType;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "createdby", length = 50)
    private String createdBy;

    // Constructors
    public FileAttachment() {}

    public FileAttachment(String id, String fileGroupId, String originalFileName, 
                         String storedFileName, String filePath, Long fileSize, 
                         String contentType, String companyId, String moduleType) {
        this.id = id;
        this.fileGroupId = fileGroupId;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.companyId = companyId;
        this.moduleType = moduleType;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileGroupId() {
        return fileGroupId;
    }

    public void setFileGroupId(String fileGroupId) {
        this.fileGroupId = fileGroupId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
