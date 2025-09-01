package com.cmms10.common.file.repository;

import com.cmms10.common.file.entity.FileAttachment;
import com.cmms10.common.file.entity.FileAttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, FileAttachmentId> {

        Optional<FileAttachment> findByCompanyIdAndFileId(String companyId, String fileId);

        List<FileAttachment> findByFileGroupId(String fileGroupId);

        List<FileAttachment> findByCompanyIdAndModuleName(String companyId, String moduleName);

        List<FileAttachment> findByCompanyIdAndFileGroupId(String companyId, String fileGroupId);

        void deleteByCompanyIdAndFileGroupId(String companyId, String fileGroupId);

        void deleteByCompanyIdAndFileId(String companyId, String fileId);

}
