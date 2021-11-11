package com.tsecho.bots.service;

import com.tsecho.bots.database.Identification;
import com.tsecho.bots.repos.UsersProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DBService {
    private final UsersProfile usersProfile;

    public DBService(UsersProfile usersProfile){
        this.usersProfile = usersProfile;
    }

    public List<Identification> getAllProfiles(){ return usersProfile.findAll(); }

    @Transactional
    public void saveUsers(Identification identification) { usersProfile.saveAndFlush(identification); }

    @Transactional
    public void deleteUsers(String id) { usersProfile.deleteById(id); }
}
