package com.tsecho.bots.repos;

import com.tsecho.bots.database.PhoneID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneID, String> {
}
