package com.cmms10.domain.user.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - UserIdClass
 * 사용자 엔티티의 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdClass implements Serializable {
    private String companyId;
    private String username;
}
