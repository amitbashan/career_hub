package com.amitbashan.career_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "recruiter_auth")
public class RecruiterAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String token;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    public RecruiterAuthToken(String token, Recruiter recruiter) {
        this.token = token;
        this.recruiter = recruiter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }
}
