package com.amitbashan.career_hub.dto;

import jakarta.validation.constraints.*;

public class CreateJobListingDTO {
    @NotBlank(message = "Job title is required")
    @Pattern(regexp = "[A-Za-z][A-Za-z ]+", message = "Job title must be a non-empty sequence of letters")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @Positive(message = "Annual salary must be positive")
    @Min(value = 15000, message = "Annual salary must be at least $15,000 ")
    @Max(value = 99999999, message = "Annual salary must be at most 8 figures")
    private Integer salary;

    public CreateJobListingDTO() {

    }

    public CreateJobListingDTO(String title, String description, Integer salary) {
        this.setTitle(title);
        this.setDescription(description);
        this.setSalary(salary);
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

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
