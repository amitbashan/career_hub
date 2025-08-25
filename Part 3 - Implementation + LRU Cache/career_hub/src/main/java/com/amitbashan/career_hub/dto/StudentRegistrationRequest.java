package com.amitbashan.career_hub.dto;

import jakarta.validation.constraints.*;

public class StudentRegistrationRequest {
    @NotBlank
    @Size(min = 3, message = "First name must be atleast 3 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, message = "Last name must be atleast 3 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$", message = "Username must start with a letter than a sequence of alphanumeric or underscore characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be atleast 8 characters and at most 20 characters")
    private String password;

    @NotBlank(message = "Educational institution is required")
    @Size(max = 50)
    private String institution;

    @NotBlank(message = "Educational qualification title is required")
    @Size(max = 50)
    private String qualification;

    @NotBlank(message = "Resume is required")
    private String resume;

    public StudentRegistrationRequest() {

    }

    public StudentRegistrationRequest(String firstName, String lastName, String username, String password, String institution, String qualification, String resume) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setPassword(password);
        this.setInstitution(institution);
        this.setQualification(qualification);
        this.setResume(resume);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
