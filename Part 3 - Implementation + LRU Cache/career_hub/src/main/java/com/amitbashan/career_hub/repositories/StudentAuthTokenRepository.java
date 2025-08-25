package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.Student;
import com.amitbashan.career_hub.entities.StudentAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudentAuthTokenRepository extends JpaRepository<StudentAuthToken, Long> {
    @Query(value = "SELECT students.* FROM students INNER JOIN student_auth ON students.id = student_auth.student_id WHERE student_auth.token = :token", nativeQuery = true)
    Student findByToken(String token);

    @Modifying
    @Query(value = "DELETE FROM student_auth WHERE student_auth.token = :token", nativeQuery = true)
    void deleteToken(String token);
}
