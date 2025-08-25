package com.amitbashan.career_hub.controllers;

import com.amitbashan.career_hub.entities.JobApplication;
import com.amitbashan.career_hub.repositories.JobApplicationRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Controller
public class JobApiController {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @GetMapping("/job/application/{id}/resume")
    public void downloadResume(@PathVariable Long id, HttpServletResponse response) {
        try {
            JobApplication jobApplication = jobApplicationRepository.getReferenceById(id);
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + jobApplication.getDate()
                            + ":ID:" + id + ":" + jobApplication.getStudentFullName() + ":resume.txt"
            );
            response.getWriter().write(new String(jobApplication.getResume(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
