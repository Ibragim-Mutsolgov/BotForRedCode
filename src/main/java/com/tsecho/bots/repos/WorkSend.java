package com.tsecho.bots.repos;

import com.tsecho.bots.database.WorkSendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSend extends JpaRepository<WorkSendId, String> {
}
