package com.cmms10.funcMaster.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FuncMasterIdClass implements Serializable {

    private String companyId;
    private String siteId;
    private String funcId;
}
