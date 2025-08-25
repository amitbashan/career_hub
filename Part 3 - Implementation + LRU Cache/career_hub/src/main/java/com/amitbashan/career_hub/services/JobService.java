package com.amitbashan.career_hub.services;

import com.amitbashan.career_hub.caching.LRUCacheAlgo;
import com.amitbashan.career_hub.dto.CreateJobListingDTO;
import com.amitbashan.career_hub.dto.JobApplicationDTO;
import com.amitbashan.career_hub.dto.JobListingDTO;
import com.amitbashan.career_hub.entities.*;
import com.amitbashan.career_hub.repositories.JobApplicationRepository;
import com.amitbashan.career_hub.repositories.JobListingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

@Service
@Transactional
public class JobService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private LRUCacheAlgo<Long, JobListing> jobListingCache;

    @Autowired
    private LRUCacheAlgo<Long, JobApplication> jobApplicationCache;

    public JobListing createJobListing(Recruiter recruiter, CreateJobListingDTO dto) {
        JobListing jobListing = new JobListing(recruiter, dto);

        try {
            return jobListingRepository.save(jobListing);
        } catch (Exception e) {
            return null;
        }
    }

    public JobListingDTO getJobListing(Long id) {
        if (id == null) {
            return null;
        }

        try {
            JobListing jobListing = jobListingCache.get(id);
            if (jobListing == null) {
                jobListing = jobListingRepository.getReferenceById(id);
                jobListingCache.put(id, jobListing);
            } else {
                System.out.println("GOT A CACHE HIT!");
            }
            return new JobListingDTO(jobListing);
        } catch (Exception e) {
            return null;
        }
    }

    public JobApplication getJobApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationCache.get(applicationId);
        if (jobApplication == null) {
            jobApplication = jobApplicationRepository.getReferenceById(applicationId);
            jobApplicationCache.put(applicationId, jobApplication);
        } else {
            System.out.println("GOT A HIT!");
        }
        return jobApplication;
    }

    public Long getNumberOfJobApplicants(Long listingId) {
        return jobApplicationRepository.getNumApplicants(listingId);
    }

    public float getRejectionRate(Long listingId) {
        return (float) jobApplicationRepository.getNumRejectedApplicants(listingId) / getNumberOfJobApplicants(listingId);
    }

    public String getRejectionRateStr(Long listingId) {
        return String.format("%.2f%%", getRejectionRate(listingId));
    }

    public JobApplicationDTO applyToJob(Student student, Long jobId) {
        try {
            JobListing jobListing = jobListingRepository.getReferenceById(jobId);
            JobApplication jobApplication = new JobApplication(ZonedDateTime.now(), JobApplicationStatus.Pending, jobListing, student, student.getResume());
            return new JobApplicationDTO(jobApplicationRepository.save(jobApplication));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
