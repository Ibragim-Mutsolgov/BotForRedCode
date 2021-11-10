package com.tsecho.bots.repos;

import com.tsecho.bots.database.SystemID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRepository extends JpaRepository<SystemID, String> {
}
