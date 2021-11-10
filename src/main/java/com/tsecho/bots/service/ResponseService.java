package com.tsecho.bots.service;

import com.tsecho.bots.database.WorkResponseId;
import com.tsecho.bots.database.WorkSendId;
import com.tsecho.bots.repos.WorkResponse;
import com.tsecho.bots.repos.WorkSend;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ResponseService {
    private final WorkResponse workResponse;

    public ResponseService(WorkResponse workResponse){
        this.workResponse = workResponse;
    }

    public List<WorkResponseId> getAllProfiles(){ return workResponse.findAll(); }

    @Transactional
    public void saveUsers(WorkResponseId workResponseId) { workResponse.save(workResponseId); }

    @Transactional
    public void deleteUsers(String id) { workResponse.deleteById(id); }
}
