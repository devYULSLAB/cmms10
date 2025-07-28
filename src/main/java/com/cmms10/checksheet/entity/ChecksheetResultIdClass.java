package com.cmms10.checksheet.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChecksheetResultIdClass implements Serializable {
    private String companyId;
    private String permitId;
}
