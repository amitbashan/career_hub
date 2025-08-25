package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.entities.RecruiterAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecruiterAuthTokenRepository extends JpaRepository<RecruiterAuthToken, Long> {
    @Query(value = "SELECT recruiters.* FROM recruiters INNER JOIN recruiter_auth ON recruiters.id = recruiter_auth.recruiter_id WHERE recruiter_auth.token = :token", nativeQuery = true)
    Recruiter findByToken(String token);

    @Modifying
    @Query(value = "DELETE FROM recruiter_auth WHERE recruiter_auth.token = :token", nativeQuery = true)
    void deleteToken(String token);
}
