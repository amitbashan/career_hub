package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    @Query(value = "SELECT * FROM recruiters r WHERE r.name = :name", nativeQuery = true)
    Recruiter findByName(String name);
}
