package com.amitbashan.career_hub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "recruiters")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String passwordHash;

    public Recruiter() {

    }

    public Recruiter(String name, String password) {
        this.name = name;
        this.setPassword(password);
    }

    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
