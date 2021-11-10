package com.tsecho.bots.service;

import com.tsecho.bots.database.WebID;
import com.tsecho.bots.database.WorkDeleteId;
import com.tsecho.bots.repos.WebRepository;
import com.tsecho.bots.repos.WorkDelete;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DeleteService {
    private WorkDelete workDelete;

    public DeleteService(WorkDelete workDelete){
        this.workDelete = workDelete;
    }

    public List<WorkDeleteId> getAllProfiles(){ return workDelete.findAll(); }

    @Transactional
    public void saveUsers(WorkDeleteId workDeleteId) { workDelete.save(workDeleteId); }

    @Transactional
    public void deleteUsers(String id) { workDelete.deleteById(id); }
}
