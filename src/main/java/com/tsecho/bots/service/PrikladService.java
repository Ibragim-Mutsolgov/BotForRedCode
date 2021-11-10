package com.tsecho.bots.service;

import com.tsecho.bots.database.Identification;
import com.tsecho.bots.database.PrikladID;
import com.tsecho.bots.repos.PrikladRepository;
import com.tsecho.bots.repos.UsersProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PrikladService {
    private PrikladRepository prikladRepository;

    public PrikladService(PrikladRepository prikladRepository){
        this.prikladRepository = prikladRepository;
    }

    public List<PrikladID> getAllProfiles(){ return prikladRepository.findAll(); }

    @Transactional
    public void saveUsers(PrikladID prikladID) { prikladRepository.save(prikladID); }

    @Transactional
    public void deleteUsers(String id) { prikladRepository.deleteById(id); }
}
