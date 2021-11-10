package com.tsecho.bots.repos;

import com.tsecho.bots.database.WebID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebRepository extends JpaRepository<WebID, String> {
}
