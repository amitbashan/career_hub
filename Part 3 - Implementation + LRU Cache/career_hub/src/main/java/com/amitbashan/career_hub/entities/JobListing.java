package com.amitbashan.career_hub.entities;

import com.amitbashan.career_hub.dto.CreateJobListingDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Entity
@Table(name = "job_listings")
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private int annualSalary;

    @NotNull
    private boolean open;

    @NotNull
    @Column(updatable = false)
    private ZonedDateTime date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    public JobListing() {

    }

    public JobListing(Recruiter recruiter, CreateJobListingDTO dto) {
        this.setTitle(dto.getTitle());
        this.setDescription(dto.getDescription());
        this.setAnnualSalary(dto.getSalary());
        this.setOpen(true);
        this.setDate(ZonedDateTime.now());
        this.setRecruiter(recruiter);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(int annualSalary) {
        this.annualSalary = annualSalary;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }
}
