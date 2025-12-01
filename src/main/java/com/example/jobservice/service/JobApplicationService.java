package com.example.jobservice.service;

import com.example.jobservice.cv.DTO.CvDto;
import com.example.jobservice.job.repository.JobRepository;
import com.example.jobservice.model.Job;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JobApplicationService {

    private final JobRepository jobRepository;
    private final CvService cvService;
    private final CvPdfGenerator cvPdfGenerator;
    private final EmailService emailService;

    public JobApplicationService(JobRepository jobRepository,
                                 CvService cvService,
                                 CvPdfGenerator cvPdfGenerator,
                                 EmailService emailService) {
        this.jobRepository = jobRepository;
        this.cvService = cvService;
        this.cvPdfGenerator = cvPdfGenerator;
        this.emailService = emailService;
    }

    public void applyToJob(Long jobId, Long userId, String userEmail) {

        // 1) Job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        String companyEmail = job.getEmail();
        if (companyEmail == null || companyEmail.isBlank()) {
            throw new RuntimeException("Job has no company email configured");
        }

        // 2) CV from cv-service
        CvDto cv = cvService.getCvByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User has no CV, cannot apply"));

        System.out.println("[DEBUG] CV found for userId " + userId + " headline: " + cv.getHeadline());

        // 3) Generate PDF from CV fields
        File cvFile = null;
        try {
            cvFile = cvPdfGenerator.generateCvPdf(cv);
            System.out.println("[DEBUG] Generated CV PDF at: " + cvFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("[WARN] Failed to generate CV PDF: " + e.getMessage());
            // still send email without attachment
        }

        // 4) Subject + body
        String subject = "New application for: " + job.getTitle();

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Hello,\n\n")
                .append("A candidate has applied for your job: \"")
                .append(job.getTitle())
                .append("\".\n\n")
                .append("Candidate email: ").append(userEmail).append("\n");

        bodyBuilder.append("\nBest regards,\nHireFire Platform");

        String body = bodyBuilder.toString();

        // 5) Send email with generated PDF
        emailService.sendJobApplication(
                companyEmail,
                userEmail,
                subject,
                body,
                cvFile   // attachment
        );
    }
}
