package com.amitbashan.career_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(updatable = false)
    private ZonedDateTime date;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private JobApplicationStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "job_listing_id")
    private JobListing jobListing;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Lob
    private byte[] resume;

    public JobApplication() {

    }

    public String getStudentFullName() {
        return String.format("%s%s", this.getStudent().getFirstName(), this.getStudent().getLastName());
    }

    public JobApplication(ZonedDateTime date, JobApplicationStatus status, JobListing jobListing, Student student, byte[] resume) {
        this.setDate(date);
        this.setStatus(status);
        this.setJobListing(jobListing);
        this.setStudent(student);
        this.setResume(resume);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public JobListing getJobListing() {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing) {
        this.jobListing = jobListing;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }
}
