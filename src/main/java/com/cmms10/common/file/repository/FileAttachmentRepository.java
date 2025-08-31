package com.cmms10.common.file.repository;

import com.cmms10.common.file.entity.FileAttachment;
import com.cmms10.common.file.entity.FileAttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

        /**
         * 특정 모듈 타입의 고아 파일 조회
         * PlantMaster 테이블에 연결되지 않은 파일들
         */
        @Query("SELECT fa FROM FileAttachment fa " +
                        "WHERE fa.moduleName = :moduleName " +
                        "AND fa.fileGroupId NOT IN " +
                        "(SELECT pm.fileGroupId FROM PlantMaster pm WHERE pm.fileGroupId IS NOT NULL) " +
                        "AND fa.createdAt < :beforeDate")
        List<FileAttachment> findOrphanFilesByModuleName(
                        @Param("moduleName") String moduleName,
                        @Param("beforeDate") LocalDateTime beforeDate);

        /**
         * 특정 모듈 타입의 고아 파일 개수 조회
         */
        @Query("SELECT COUNT(fa) FROM FileAttachment fa " +
                        "WHERE fa.moduleName = :moduleName " +
                        "AND fa.fileGroupId NOT IN " +
                        "(SELECT pm.fileGroupId FROM PlantMaster pm WHERE pm.fileGroupId IS NOT NULL) " +
                        "AND fa.createdAt < :beforeDate")
        long countOrphanFilesByModuleName(
                        @Param("moduleName") String moduleName,
                        @Param("beforeDate") LocalDateTime beforeDate);

        /**
         * 특정 fileGroupId가 실제로 사용되고 있는지 확인
         */
        @Query("SELECT CASE WHEN COUNT(pm) > 0 THEN true ELSE false END " +
                        "FROM PlantMaster pm WHERE pm.fileGroupId = :fileGroupId")
        boolean isFileGroupIdUsed(@Param("fileGroupId") String fileGroupId);
}
