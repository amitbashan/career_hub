package com.amitbashan.career_hub.services;

import com.amitbashan.career_hub.AppSettings;
import com.amitbashan.career_hub.components.AuthUtil;
import com.amitbashan.career_hub.dto.*;
import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.entities.RecruiterAuthToken;
import com.amitbashan.career_hub.entities.Student;
import com.amitbashan.career_hub.entities.StudentAuthToken;
import com.amitbashan.career_hub.repositories.RecruiterAuthTokenRepository;
import com.amitbashan.career_hub.repositories.RecruiterRepository;
import com.amitbashan.career_hub.repositories.StudentAuthTokenRepository;
import com.amitbashan.career_hub.repositories.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import java.io.IOException;
import java.util.Set;

@Service
@Transactional
public class AuthService {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private StudentAuthTokenRepository studentAuthTokenRepository;

    @Autowired
    private RecruiterAuthTokenRepository recruiterAuthTokenRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private Validator validator;

    public LoginResponse<StudentRegistrationRequest> register(StudentRegistrationRequest req) {
        Set<ConstraintViolation<StudentRegistrationRequest>> violations = validator.validate(req);

        if (!violations.isEmpty()) {
            return new LoginResponse<>(false, "Invalid request fields", violations, null);
        }

        if (studentRepository.findByUsername(req.getUsername()) != null) {
            return new LoginResponse<>(false, "Username is already in use", violations, null);
        }

        Student student = new Student(req.getFirstName(), req.getLastName(), req.getUsername(), req.getPassword(), req.getInstitution(), req.getQualification(), req.getResume().getBytes());
        studentRepository.save(student);
        String token = generateToken(student);

        return new LoginResponse<>(true, "Registered student successfully", violations, token);
    }

    public LoginResponse<RecruiterRegistrationRequest> register(RecruiterRegistrationRequest req) {
        Set<ConstraintViolation<RecruiterRegistrationRequest>> violations = validator.validate(req);

        if (!violations.isEmpty()) {
            return new LoginResponse<>(false, "Invalid request fields", violations, null);
        }

        if (recruiterRepository.findByName(req.getName()) != null) {
            return new LoginResponse<>(false, "Name is already in use", violations, null);
        }

        Recruiter recruiter = new Recruiter(req.getName(), req.getPassword());
        recruiterRepository.save(recruiter);
        String token = generateToken(recruiter);

        return new LoginResponse<>(true, "Registered recruiter successfully", violations, token);
    }

    public LoginResponse<StudentLoginRequest> login(StudentLoginRequest req) {
        Set<ConstraintViolation<StudentLoginRequest>> violations = validator.validate(req);

        if (!violations.isEmpty()) {
            return new LoginResponse<>(false, "Invalid request fields", violations, null);
        }

        Student student = studentRepository.findByUsername(req.getUsername());

        if (student == null || !BCrypt.checkpw(req.getPassword(), student.getPasswordHash())) {
            return new LoginResponse<>(false, "Username or password are incorrect", violations, null);
        }

        String token = generateToken(student);

        return new LoginResponse<>(true, "Logged in successfully", violations, token);
    }

    public LoginResponse<RecruiterLoginRequest> login(RecruiterLoginRequest req) {
        Set<ConstraintViolation<RecruiterLoginRequest>> violations = validator.validate(req);

        if (!violations.isEmpty()) {
            return new LoginResponse<>(false, "Invalid request fields", violations, null);
        }

        Recruiter recruiter = recruiterRepository.findByName(req.getName());

        if (recruiter == null || !BCrypt.checkpw(req.getPassword(), recruiter.getPasswordHash())) {
            return new LoginResponse<>(false, "Username or password are incorrect", violations, null);
        }

        String token = generateToken(recruiter);

        return new LoginResponse<>(true, "Logged in successfully", violations, token);
    }

    public Student authorizeStudent(String token) {
        if (token == null) {
            return null;
        }

        Student student = studentAuthTokenRepository.findByToken(token);

        if (student == null || !authUtil.verify(token)) {
            return null;
        }

        return student;
    }

    public Recruiter authorizeRecruiter(String token) {
        if (token == null) {
            return null;
        }

        Recruiter recruiter = recruiterAuthTokenRepository.findByToken(token);

        if (recruiter == null || !authUtil.verify(token)) {
            return null;
        }

        return recruiter;
    }

    public String generateToken(Student student) {
        String token = authUtil.generate(student.getUsername(), false);
        studentAuthTokenRepository.save(new StudentAuthToken(token, student));
        return token;
    }

    public String generateToken(Recruiter recruiter) {
        String token = authUtil.generate(recruiter.getName(), true);
        recruiterAuthTokenRepository.save(new RecruiterAuthToken(token, recruiter));
        return token;
    }

    public void invalidateToken(String token) {
        if (studentAuthTokenRepository.findByToken(token) != null) {
            studentAuthTokenRepository.deleteToken(token);
        } else if (recruiterAuthTokenRepository.findByToken(token) != null) {
            recruiterAuthTokenRepository.deleteToken(token);
        }
    }

    public Cookie makeAuthCookie(String token) {
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(!activeProfile.equalsIgnoreCase("dev"));
        cookie.setMaxAge(AppSettings.AUTH_TOKEN_VALID_DURATION_HOURS * 60 * 60);
        return cookie;
    }
}
