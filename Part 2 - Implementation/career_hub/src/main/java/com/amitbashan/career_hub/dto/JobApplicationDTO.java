package com.amitbashan.career_hub.dto;

import com.amitbashan.career_hub.entities.JobApplication;
import com.amitbashan.career_hub.entities.JobApplicationStatus;

import java.time.ZonedDateTime;

public record JobApplicationDTO(Long id, ZonedDateTime date, JobApplicationStatus status, JobListingDTO jobListing,
                                StudentDTO student) {
    public JobApplicationDTO(JobApplication jobApplication) {
        this(jobApplication.getId(),
                jobApplication.getDate(),
                jobApplication.getStatus(),
                new JobListingDTO(jobApplication.getJobListing()),
                new StudentDTO(jobApplication.getStudent())
        );
    }
}
