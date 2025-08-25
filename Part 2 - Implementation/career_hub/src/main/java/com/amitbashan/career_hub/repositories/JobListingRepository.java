package com.amitbashan.career_hub.repositories;

import com.amitbashan.career_hub.entities.JobListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    @Query(value = "SELECT * FROM job_listings ORDER BY l.date DESC LIMIT :n", nativeQuery = true)
    List<JobListing> getRecentListings(int n);

    Page<JobListing> findByRecruiterId(Long id, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE job_listings listing SET listing.open = :value WHERE listing.id = :id", nativeQuery = true)
    void updateOpenById(Long id, boolean value);
}
