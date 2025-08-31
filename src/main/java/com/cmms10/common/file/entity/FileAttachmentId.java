package com.cmms10.common.file.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * FileAttachment 엔티티의 복합 PK 클래스
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachmentId implements Serializable {

    private String companyId; // 회사 ID
    private String fileId; // 파일 ID
}
