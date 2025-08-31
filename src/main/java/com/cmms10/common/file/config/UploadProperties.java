/**
 * 프로그램명: 업로드 속성 바인딩
 * 기능: cmms.file.upload.* 설정을 바인딩하여 모듈별 제한값 제공
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-08-29
 */
package com.cmms10.common.file.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cmms.file.upload")
public class UploadProperties {

    /**
     * modules 키: 모듈명(예: plantMaster) → FileUploadModuleProps
     */
    private Map<String, FileUploadModuleProps> modules;

    public Map<String, FileUploadModuleProps> getModules() {
        return modules;
    }

    public void setModules(Map<String, FileUploadModuleProps> modules) {
        this.modules = modules;
    }

    public FileUploadModuleProps getModule(String moduleName) {
        if (modules == null)
            return null;
        return modules.get(moduleName);
    }
}
