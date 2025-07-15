package com.cmms10.commonCode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeIdClass implements Serializable {
    private String companyId;
    private String codeId;
}
