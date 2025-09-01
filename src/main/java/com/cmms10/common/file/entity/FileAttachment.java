package com.cmms10.common.file.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * cmms10 - FileAttachment
 * 파일 첨부 정보를 관리하는 엔티티
 */
@Entity
@Table(name = "fileattachment")
@IdClass(FileAttachmentId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {

    @Id
    @Column(name = "companyId", length = 10, nullable = false)
    private String companyId; // 회사 ID (복합 PK)

    @Id
    @Column(name = "fileId", length = 10, nullable = false)
    private String fileId; // 파일 ID (복합 PK)

    @Column(name = "fileGroupId", length = 10, nullable = false)
    private String fileGroupId; // 파일 그룹 ID

    @Column(name = "originalFileName", length = 255, nullable = false)
    private String originalFileName; // 원본 파일명

    @Column(name = "storedFileName", length = 255, nullable = false)
    private String storedFileName; // 저장된 파일명 (UUID + 확장자)

    @Column(name = "filePath", length = 500, nullable = false)
    private String filePath; // 파일 저장 경로

    @Column(name = "fileSize")
    private Long fileSize; // 파일 크기 (바이트)

    @Column(name = "contentType", length = 100)
    private String contentType; // 파일 MIME 타입

    @Column(name = "moduleName", length = 50, nullable = false)
    private String moduleName; // 모듈명 (어떤 기능에서 업로드된 파일인지)

    @Column(name = "createDate")
    private LocalDateTime createDate; // 생성일시

    @Column(name = "createBy", length = 50)
    private String createdBy; // 생성자
}
