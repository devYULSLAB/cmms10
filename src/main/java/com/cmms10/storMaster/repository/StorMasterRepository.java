package com.cmms10.storMaster.repository;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorMasterRepository extends JpaRepository<StorMaster, StorMasterId> {
} 