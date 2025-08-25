package com.amitbashan.career_hub.controllers;

import com.amitbashan.career_hub.dto.StudentDTO;
import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.entities.RecruiterDTO;
import com.amitbashan.career_hub.entities.Student;
import com.amitbashan.career_hub.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AuthControllerAdvice {
    @Autowired
    private AuthService authService;

    @ModelAttribute("userRole")
    public String userRole(@CookieValue(name = "Authorization", required = false) String token) {
        if (authService.authorizeStudent(token) != null) {
            return "student";
        }

        if (authService.authorizeRecruiter(token) != null) {
            return "recruiter";
        }

        return "guest";
    }

    @ModelAttribute("student")
    public StudentDTO student(@CookieValue(name = "Authorization", required = false) String token) {
        Student student = authService.authorizeStudent(token);
        if (student == null) {
            return null;
        }
        return new StudentDTO(student);
    }

    @ModelAttribute("recruiter")
    public RecruiterDTO recruiter(@CookieValue(name = "Authorization", required = false) String token) {
        Recruiter recruiter = authService.authorizeRecruiter(token);
        if (recruiter == null) {
            return null;
        }
        return new RecruiterDTO(recruiter);
    }

}
