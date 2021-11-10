package com.tsecho.bots.service;

import com.tsecho.bots.database.GameID;
import com.tsecho.bots.database.PrikladID;
import com.tsecho.bots.repos.GameRepository;
import com.tsecho.bots.repos.PrikladRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GameService {
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public List<GameID> getAllProfiles(){ return gameRepository.findAll(); }

    @Transactional
    public void saveUsers(GameID gameID) { gameRepository.save(gameID); }

    @Transactional
    public void deleteUsers(String id) { gameRepository.deleteById(id); }
}
