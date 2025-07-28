package com.cmms10.checksheet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "checksheettemplate")
public class ChecksheetTemplate {

    @Id
    @Column(length = 10)
    private String templateId;

    @Column(length = 5, nullable = false)
    private String companyId;

    @Column(length = 100)
    private String templateName;

    @Lob
    private String templateJson; // formBuilder.js 결과(JSON) 전체 저장

    @Column(length = 5)
    private String createBy;

    private LocalDateTime createDate;
}
