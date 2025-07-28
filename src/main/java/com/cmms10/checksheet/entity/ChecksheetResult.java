package com.cmms10.checksheet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@IdClass(ChecksheetResultIdClass.class)
@Table(name = "checksheetresult")
public class ChecksheetResult {

    @Id
    @Column(length = 5)
    private String companyId;

    @Id
    @Column(length = 10)
    private String permitId;

    @Column(length = 10)
    private String templateId;

    @Lob
    private String checkResultJson;

    @Column(length = 5)
    private String createBy;

    private LocalDateTime createDate;
}
