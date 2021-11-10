package com.tsecho.bots.service;

import com.tsecho.bots.database.WebID;
import com.tsecho.bots.repos.WebRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WebService {
    private WebRepository webRepository;

    public WebService(WebRepository webRepository){
        this.webRepository = webRepository;
    }

    public List<WebID> getAllProfiles(){ return webRepository.findAll(); }

    @Transactional
    public void saveUsers(WebID webID) { webRepository.save(webID); }

    @Transactional
    public void deleteUsers(String id) { webRepository.deleteById(id); }
}
