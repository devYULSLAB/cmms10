package com.cmms10.inspection.entity; // << UPDATED PACKAGE

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionItemIdClass implements Serializable {
    private String companyId;
    private String inspectionId;
    private String itemId; // DB is CHAR(2)
}
