package com.amitbashan.career_hub.entities;

import com.amitbashan.career_hub.services.AuthService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String passwordHash;

    @NotBlank
    private String institution;

    @NotBlank
    private String qualification;

    @Lob
    private byte[] resume;

    public Student() {
    }

    public Student(String firstName, String lastName, String username, String password, String institution, String qualification, byte[] resume) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setPassword(password);
        this.institution = institution;
        this.qualification = qualification;
        this.resume = resume;
    }

    public Long getId() {
        return id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }
}
