package com.cmms10.common.file.repository;

import com.cmms10.common.file.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, String> {
    List<FileAttachment> findByFileGroupId(String fileGroupId);

    List<FileAttachment> findByCompanyIdAndModuleType(String companyId, String moduleType);

    List<FileAttachment> findByFileGroupIdAndCompanyId(String fileGroupId, String companyId);

    void deleteByFileGroupId(String fileGroupId);

    void deleteByFileGroupIdAndCompanyId(String fileGroupId, String companyId);

    /**
     * 특정 모듈 타입의 고아 파일 조회
     * PlantMaster 테이블에 연결되지 않은 파일들
     */
    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.moduleType = :moduleType " +
            "AND fa.fileGroupId NOT IN " +
            "(SELECT pm.fileGroupId FROM PlantMaster pm WHERE pm.fileGroupId IS NOT NULL) " +
            "AND fa.createdAt < :beforeDate")
    List<FileAttachment> findOrphanFilesByModuleType(
            @Param("moduleType") String moduleType,
            @Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 특정 모듈 타입의 고아 파일 개수 조회
     */
    @Query("SELECT COUNT(fa) FROM FileAttachment fa " +
            "WHERE fa.moduleType = :moduleType " +
            "AND fa.fileGroupId NOT IN " +
            "(SELECT pm.fileGroupId FROM PlantMaster pm WHERE pm.fileGroupId IS NOT NULL) " +
            "AND fa.createdAt < :beforeDate")
    long countOrphanFilesByModuleType(
            @Param("moduleType") String moduleType,
            @Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 특정 fileGroupId가 실제로 사용되고 있는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(pm) > 0 THEN true ELSE false END " +
            "FROM PlantMaster pm WHERE pm.fileGroupId = :fileGroupId")
    boolean isFileGroupIdUsed(@Param("fileGroupId") String fileGroupId);
}
