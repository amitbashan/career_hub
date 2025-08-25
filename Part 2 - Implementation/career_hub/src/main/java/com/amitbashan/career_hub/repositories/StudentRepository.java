package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM students s WHERE s.username = :un", nativeQuery = true)
    Student findByUsername(String un);
}
