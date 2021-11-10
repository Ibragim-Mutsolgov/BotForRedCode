package com.tsecho.bots.service;

import com.tsecho.bots.database.SystemID;
import com.tsecho.bots.database.WorkSendId;
import com.tsecho.bots.repos.SystemRepository;
import com.tsecho.bots.repos.WorkSend;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SendService {
    private final WorkSend workSend;

    public SendService(WorkSend workSend){
        this.workSend = workSend;
    }

    public List<WorkSendId> getAllProfiles(){ return workSend.findAll(); }

    @Transactional
    public void saveUsers(WorkSendId workSendId) { workSend.save(workSendId); }

    @Transactional
    public void deleteUsers(String id) { workSend.deleteById(id); }
}
