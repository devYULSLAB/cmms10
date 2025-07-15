package com.cmms10.storMaster.service;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterId;
import com.cmms10.storMaster.repository.StorMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorMasterService {
    private final StorMasterRepository storMasterRepository;

    public StorMasterService(StorMasterRepository storMasterRepository) {
        this.storMasterRepository = storMasterRepository;
    }

    public List<StorMaster> findAll() {
        return storMasterRepository.findAll();
    }

    public Optional<StorMaster> findById(StorMasterId id) {
        return storMasterRepository.findById(id);
    }

    public StorMaster save(StorMaster storMaster) {
        return storMasterRepository.save(storMaster);
    }

    public void deleteById(StorMasterId id) {
        storMasterRepository.deleteById(id);
    }
} 