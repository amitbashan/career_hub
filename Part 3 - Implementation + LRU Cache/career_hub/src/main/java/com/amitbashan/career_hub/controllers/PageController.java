package com.amitbashan.career_hub.controllers;

import com.amitbashan.career_hub.dto.*;
import com.amitbashan.career_hub.entities.JobListing;
import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.services.AuthService;
import com.amitbashan.career_hub.services.JobService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public String index() {
        return "pages/index";
    }

    @GetMapping("/about")
    public String about() {
        return "pages/about";
    }

    @GetMapping("/student/signup")
    public String student_signup(Model model) {
        model.addAttribute("registrationRequest", new StudentRegistrationRequest());
        return "pages/signup/student";
    }

    @PostMapping("/student/signup")
    public String student_register(@Valid @ModelAttribute("registrationRequest") StudentRegistrationRequest registrationRequest, BindingResult bindingResult, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "pages/signup/student";
        }

        LoginResponse<StudentRegistrationRequest> resp = authService.register(registrationRequest);

        if (resp.success()) {
            model.addAttribute("redirectPath", "/student/login");
            response.addCookie(authService.makeAuthCookie(resp.authToken()));
            return "pages/signup/success";
        }

        bindingResult.addError(new FieldError("loginRequest", "username", "Username already taken"));
        model.addAttribute("registrationRequest", registrationRequest);
        return "pages/signup/student";
    }

    @GetMapping("/recruiter/signup")
    public String recruiter_signup(Model model) {
        model.addAttribute("registrationRequest", new RecruiterRegistrationRequest());
        return "pages/signup/recruiter";
    }

    @PostMapping("/recruiter/signup")
    public String recruiter_register(@Valid @ModelAttribute("registrationRequest") RecruiterRegistrationRequest registrationRequest, BindingResult bindingResult, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "pages/signup/recruiter";
        }

        LoginResponse<RecruiterRegistrationRequest> resp = authService.register(registrationRequest);

        if (resp.success()) {
            model.addAttribute("redirectPath", "/recruiter/login");
            response.addCookie(authService.makeAuthCookie(resp.authToken()));
            return "pages/signup/success";
        }

        bindingResult.addError(new FieldError("loginRequest", "name", "Name already taken"));
        model.addAttribute("registrationRequest", registrationRequest);
        return "pages/signup/recruiter";
    }

    @GetMapping("/student/login")
    public String student_login_page(Model model) {
        model.addAttribute("loginRequest", new StudentLoginRequest());
        return "pages/login/student";
    }

    @PostMapping("/student/login")
    public String student_login(@Valid @ModelAttribute("loginRequest") StudentLoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "pages/login/student";
        }

        LoginResponse<StudentLoginRequest> resp = authService.login(loginRequest);

        if (resp.success()) {
            response.addCookie(authService.makeAuthCookie(resp.authToken()));
            return "redirect:/";
        }

        return "pages/login/student";
    }

    @PostMapping("/recruiter/login")
    public String recruiter_login(@Valid @ModelAttribute("loginRequest") RecruiterLoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "pages/login/recruiter";
        }

        LoginResponse<RecruiterLoginRequest> resp = authService.login(loginRequest);

        if (resp.success()) {
            response.addCookie(authService.makeAuthCookie(resp.authToken()));
            return "redirect:/";
        }

        return "pages/login/recruiter";
    }

    @GetMapping("/recruiter/login")
    public String recruiter_login_page(Model model) {
        model.addAttribute("loginRequest", new RecruiterLoginRequest());
        return "pages/login/recruiter";
    }


    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @PostMapping("/signout")
    public String signout(@CookieValue(name = "Authorization", required = false) String token, HttpServletResponse response) {
        if (token != null) {
            authService.invalidateToken(token);
            Cookie cookie = new Cookie("Authorization", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}
