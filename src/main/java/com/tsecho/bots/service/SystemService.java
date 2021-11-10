package com.tsecho.bots.service;

import com.tsecho.bots.database.SystemID;
import com.tsecho.bots.repos.SystemRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SystemService {
    private final SystemRepository systemRepository;

    public SystemService(SystemRepository systemRepository){
        this.systemRepository = systemRepository;
    }

    public List<SystemID> getAllProfiles(){ return systemRepository.findAll(); }

    @Transactional
    public void saveUsers(SystemID systemID) { systemRepository.save(systemID); }

    @Transactional
    public void deleteUsers(String id) { systemRepository.deleteById(id); }
}
