package com.tsecho.bots.repos;

import com.tsecho.bots.database.PrikladID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrikladRepository extends JpaRepository<PrikladID, String> {
}
