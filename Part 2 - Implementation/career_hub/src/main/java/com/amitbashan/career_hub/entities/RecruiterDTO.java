package com.amitbashan.career_hub.entities;

public record RecruiterDTO(Long id, String name) {
    public RecruiterDTO(Recruiter recruiter) {
        this(recruiter.getId(), recruiter.getName());
    }
}
