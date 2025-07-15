package com.cmms10.storMaster.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorMasterId implements Serializable {
    private String companyId;
    private String siteId;
    private String locId;
} 