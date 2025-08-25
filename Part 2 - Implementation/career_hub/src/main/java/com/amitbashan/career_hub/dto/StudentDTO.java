package com.amitbashan.career_hub.dto;

import com.amitbashan.career_hub.entities.Student;

public record StudentDTO(Long id, String firstName, String lastName, String username, String institution,
                         String qualification) {
    public StudentDTO(Student student) {
        this(student.getId(), student.getFirstName(), student.getLastName(), student.getUsername(), student.getInstitution(), student.getQualification());
    }

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
