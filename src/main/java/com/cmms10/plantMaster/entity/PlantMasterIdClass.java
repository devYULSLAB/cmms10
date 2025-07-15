package com.cmms10.plantMaster.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantMasterIdClass implements Serializable {
    private String companyId;
    private String siteId;
    private String plantId;
}
