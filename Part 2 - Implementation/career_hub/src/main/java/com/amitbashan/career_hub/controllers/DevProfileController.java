package com.amitbashan.career_hub.controllers;

import com.amitbashan.career_hub.dto.CreateJobListingDTO;
import com.amitbashan.career_hub.dto.LoginResponse;
import com.amitbashan.career_hub.dto.RecruiterRegistrationRequest;
import com.amitbashan.career_hub.dto.StudentRegistrationRequest;
import com.amitbashan.career_hub.entities.Recruiter;
import com.amitbashan.career_hub.entities.Student;
import com.amitbashan.career_hub.services.AuthService;
import com.amitbashan.career_hub.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DevProfileController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JobService jobService;

    @GetMapping("/seed")
    public String seedDatabase() {
        RecruiterRegistrationRequest recruiterRegistrationRequest = new RecruiterRegistrationRequest(
                "Microsoft",
                "macrohard"
        );
        RecruiterRegistrationRequest appleRecruiterRegistrationRequest = new RecruiterRegistrationRequest(
                "Apple",
                "stevewozniak"
        );
        CreateJobListingDTO softwareEngineerJobListing = new CreateJobListingDTO(
                "Junior Software Engineer",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                20000
        );
        CreateJobListingDTO seniorSoftwareEngineerJobListing = new CreateJobListingDTO(
                "Senior Software Engineer",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                50000
        );
        CreateJobListingDTO vpSoftwareEngineeringJobListing = new CreateJobListingDTO(
                "VP of Engineering",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                100000
        );

        StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest(
                "Amit",
                "Bashan",
                "amitbashan",
                "bashan123",
                "Netanya Academic College",
                "BSc Computer Science",
                "Hello world!"
        );


        String recruiterToken = authService.register(recruiterRegistrationRequest).authToken();
        Recruiter recruiter = authService.authorizeRecruiter(recruiterToken);

        String appleToken = authService.register(appleRecruiterRegistrationRequest).authToken();
        Recruiter apple = authService.authorizeRecruiter(appleToken);

        jobService.createJobListing(recruiter, softwareEngineerJobListing);
        jobService.createJobListing(recruiter, seniorSoftwareEngineerJobListing);

        jobService.createJobListing(apple, softwareEngineerJobListing);
        jobService.createJobListing(apple, seniorSoftwareEngineerJobListing);
        jobService.createJobListing(apple, vpSoftwareEngineeringJobListing);

        authService.register(studentRegistrationRequest);

        return "redirect:/";
    }
}
