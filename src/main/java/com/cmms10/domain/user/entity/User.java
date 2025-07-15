package com.cmms10.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * cmms10 - User
 * 사용자 정보 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"") // Enclosed in quotes because "user" is often a reserved keyword
@IdClass(UserIdClass.class)
public class User {

    @Id
    @Column(name = "companyId", length = 5)
    private String companyId;

    @Id
    @Column(name = "username", length = 5)
    private String username;

    @Column(name = "userFullName", length = 100)
    private String userFullName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "deptId", length = 5)
    private String deptId;

    @Column(name = "roleId", length = 5)
    private String roleId;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "fileGroupId", length = 10)
    private String fileGroupId;

    @Column(name = "createBy", length = 5)
    private String createBy;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @Column(name = "updateBy", length = 5)
    private String updateBy;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "deleteMark", length = 1)
    private String deleteMark;

    // Constructors
}
