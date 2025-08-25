package com.amitbashan.career_hub.controllers;

import com.amitbashan.career_hub.dto.CreateJobListingDTO;
import com.amitbashan.career_hub.dto.JobApplicationDTO;
import com.amitbashan.career_hub.dto.JobListingDTO;
import com.amitbashan.career_hub.entities.*;
import com.amitbashan.career_hub.repositories.JobApplicationRepository;
import com.amitbashan.career_hub.repositories.JobListingRepository;
import com.amitbashan.career_hub.services.AuthService;
import com.amitbashan.career_hub.services.JobService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@Transactional
public class JobPageController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @GetMapping("/job/listing/{id}")
    public String viewJobListing(@PathVariable Long id, Model model) {
        JobListingDTO dto = jobService.getJobListing(id);

        if (dto == null) {
            return "error";
        }

        model.addAttribute("jobListing", dto);
        model.addAttribute("numApplicants", jobService.getNumberOfJobApplicants(id));
        model.addAttribute("rejectionRate", jobService.getRejectionRateStr(id));
        model.addAttribute("openingDate", dto.date().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return "pages/jobListing";
    }

    @GetMapping("/job/listing")
    public String newJobListing(@CookieValue(name = "Authorization", required = false) String token, Model model) {
        if (authService.authorizeRecruiter(token) == null) {
            return "error";
        }

        model.addAttribute("jobListing", new CreateJobListingDTO());

        return "pages/newJobListing";
    }

    @PostMapping("/job/listing")
    public String createJobListing(@CookieValue(name = "Authorization", required = false) String token, @Valid @ModelAttribute("jobListing") CreateJobListingDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/newJobListing";
        }

        Recruiter recruiter = authService.authorizeRecruiter(token);

        if (recruiter == null) {
            return "error";
        }

        JobListing jobListing = jobService.createJobListing(recruiter, dto);

        if (jobListing != null) {
            return "redirect:/job/listing/" + jobListing.getId();
        }

        return "error";
    }

    @GetMapping("/listings")
    public String listings(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageRequest = PageRequest.of(page, 10, Sort.by("date"));
        Page<JobListing> jobListings = jobListingRepository.findAll(pageRequest);

        model.addAttribute("listings", jobListings);
        model.addAttribute("numPages", jobListings.getTotalPages());
        model.addAttribute("page", page);

        return "pages/listings";
    }

    @GetMapping("/listings/mine")
    public String my_listings(@RequestParam(defaultValue = "0") int page, @CookieValue(name = "Authorization", required = false) String token, Model model) {
        Recruiter recruiter = authService.authorizeRecruiter(token);

        if (recruiter == null) {
            return "error";
        }

        Pageable pageRequest = PageRequest.of(page, 10, Sort.by("date"));
        Page<JobListing> jobListings = jobListingRepository.findByRecruiterId(recruiter.getId(), pageRequest);
        List<JobListingDTO> dtos = jobListings.stream().map(JobListingDTO::new).toList();

        model.addAttribute("listings", dtos);
        model.addAttribute("numPages", jobListings.getTotalPages());
        model.addAttribute("page", page);

        return "pages/myListings";
    }

    @PostMapping("/job/listing/{id}")
    public String lock_listing(@PathVariable Long id, @RequestParam(required = false) Boolean lock, @RequestParam(required = false) Boolean delete, @CookieValue(name = "Authorization", required = false) String token) {
        Recruiter recruiter = authService.authorizeRecruiter(token);

        if (recruiter == null) {
            return "error";
        }

        if (lock != null) {
            jobListingRepository.updateOpenById(id, lock);
        }

        if (delete) {
            jobListingRepository.deleteById(id);
            return "redirect:/listings/mine";
        }

        return "redirect:/job/listing/" + id;
    }

    @PostMapping("/job/listing/{id}/apply")
    public String submit_application(@CookieValue(name = "Authorization", required = false) String token, @PathVariable Long id) {
        Student student = authService.authorizeStudent(token);

        if (student == null) {
            return "error";
        }

        JobApplicationDTO dto = jobService.applyToJob(student, id);

        if (dto == null) {
            return "error";
        }

        return "redirect:/applications";
    }

    @GetMapping("/applications")
    public String applications(@RequestParam(defaultValue = "0") int page, @CookieValue(name = "Authorization", required = false) String token, Model model) {
        Recruiter recruiter = authService.authorizeRecruiter(token);
        Student student = authService.authorizeStudent(token);
        Pageable pageRequest = PageRequest.of(page, 10, Sort.by("date"));
        Page<JobApplication> jobApplicationPage;

        if (recruiter != null) {
            jobApplicationPage = jobApplicationRepository.findAllByJobListingRecruiterId(recruiter.getId(), pageRequest);
        } else if (student != null) {
            jobApplicationPage = jobApplicationRepository.findAllByStudentId(student.getId(), pageRequest);
        } else {
            return "error";
        }

        List<JobApplicationDTO> jobApplications = jobApplicationPage.stream().map(JobApplicationDTO::new).toList();

        model.addAttribute("apps", jobApplications);
        model.addAttribute("numPages", jobApplicationPage.getTotalPages());
        model.addAttribute("page", page);

        return "pages/applications";
    }

    @PostMapping("/job/application/{id}")
    public String cancel_application(@PathVariable Long id, @RequestParam(required = false) Boolean delete, @RequestParam(required = false) Boolean accept, @CookieValue(name = "Authorization", required = false) String token) {
        Pair<Boolean, Boolean> permPair = authService.verifyMutualRecruiterStudentPermission(id, token);

        if (permPair == null) {
            return "error";
        }

        boolean studentVerified = permPair.getSecond();
        boolean recruiterVerified = permPair.getFirst();
        boolean verified = recruiterVerified || studentVerified;

        if (verified && delete != null && delete) {
            jobApplicationRepository.updateStatusById(id, JobApplicationStatus.Rejected);
        } else if (recruiterVerified && accept) {
            jobApplicationRepository.updateStatusById(id, JobApplicationStatus.Accepted);
        }

        return "redirect:/applications";
    }
}
