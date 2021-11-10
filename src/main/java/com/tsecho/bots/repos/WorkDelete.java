package com.tsecho.bots.repos;

import com.tsecho.bots.database.WorkDeleteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDelete extends JpaRepository<WorkDeleteId, String> {
}
