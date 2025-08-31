/**
 * 프로그램명: 파일 업로드 모듈 속성(단일 모듈)
 * 기능: application.yml의 cmms.file.upload.modules.* 바인딩용 DTO
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-08-29
 */
package com.cmms10.common.file.config;

public class FileUploadModuleProps {
    private String accept;
    private Integer maxFiles;
    private Integer maxTotalMb;

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public Integer getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(Integer maxFiles) {
        this.maxFiles = maxFiles;
    }

    public Integer getMaxTotalMb() {
        return maxTotalMb;
    }

    public void setMaxTotalMb(Integer maxTotalMb) {
        this.maxTotalMb = maxTotalMb;
    }
}
