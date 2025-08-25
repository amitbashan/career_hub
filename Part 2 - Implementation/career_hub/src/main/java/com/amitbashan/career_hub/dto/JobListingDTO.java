package com.amitbashan.career_hub.dto;

import com.amitbashan.career_hub.entities.JobListing;

import java.time.ZonedDateTime;

public record JobListingDTO(Long id, String title, String description, int annualSalary, boolean open, ZonedDateTime date,
                            RecruiterDTO recruiter) {
    public JobListingDTO(JobListing listing) {
        this(listing.getId(), listing.getTitle(), listing.getDescription(), listing.getAnnualSalary(), listing.isOpen(), listing.getDate(), new RecruiterDTO(listing.getRecruiter()));
    }
}
