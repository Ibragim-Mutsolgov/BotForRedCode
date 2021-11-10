package com.tsecho.bots.repos;

import com.tsecho.bots.database.WorkResponseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkResponse extends JpaRepository<WorkResponseId, String> {
}
