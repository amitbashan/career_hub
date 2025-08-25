package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.JobApplication;
import com.amitbashan.career_hub.entities.JobApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query(value = "SELECT * FROM job_applications a WHERE a.student_id = :id", nativeQuery = true)
    List<JobApplication> findByStudent(Long id);

    @Query(value = "SELECT * FROM job_applications a WHERE a.student_id = :id ORDER BY a.date DESC LIMIT :n", nativeQuery = true)
    List<JobApplication> findRecentByStudent(Long id, int n);

    @Query(value = "SELECT COUNT(*) FROM job_applications a WHERE a.job_listing_id = :jobId", nativeQuery = true)
    Long getNumApplicants(Long jobId);

    @Query(value = "SELECT COUNT(*) FROM job_applications a WHERE a.job_listing_id = :jobId AND a.status = 2", nativeQuery = true)
    Long getNumRejectedApplicants(Long jobId);

    Page<JobApplication> findAllByJobListingRecruiterId(Long id, Pageable pageable);

    Page<JobApplication> findAllByStudentId(Long id, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE job_applications a SET a.status = :status WHERE a.id = :id", nativeQuery = true)
    void updateStatusById(Long id, JobApplicationStatus status);
}
