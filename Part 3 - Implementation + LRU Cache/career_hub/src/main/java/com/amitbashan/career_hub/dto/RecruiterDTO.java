package com.amitbashan.career_hub.dto;

import com.amitbashan.career_hub.entities.Recruiter;

public record RecruiterDTO(Long id, String name) {
    public RecruiterDTO(Recruiter recruiter) {
        this(recruiter.getId(), recruiter.getName());
    }
}
