package com.tsecho.bots.repos;

import com.tsecho.bots.database.Identification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersProfile extends JpaRepository<Identification, String> {
}
