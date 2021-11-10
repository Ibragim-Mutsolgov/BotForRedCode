package com.tsecho.bots.service;

import com.tsecho.bots.database.PhoneID;
import com.tsecho.bots.database.PrikladID;
import com.tsecho.bots.repos.PhoneRepository;
import com.tsecho.bots.repos.PrikladRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PhoneService {
    private PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository){
        this.phoneRepository = phoneRepository;
    }

    public List<PhoneID> getAllProfiles(){ return phoneRepository.findAll(); }

    @Transactional
    public void saveUsers(PhoneID phoneID) { phoneRepository.save(phoneID); }

    @Transactional
    public void deleteUsers(String id) { phoneRepository.deleteById(id); }
}
