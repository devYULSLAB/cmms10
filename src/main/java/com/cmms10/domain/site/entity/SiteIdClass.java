package com.cmms10.domain.site.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - SiteIdClass
 * 사이트 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteIdClass implements Serializable {
    private String companyId;
    private String siteId;
}
