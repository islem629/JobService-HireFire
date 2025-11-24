package com.example.jobservice.service;

import com.example.jobservice.cv.client.CvClient;
import com.example.jobservice.cv.DTO.*;
import com.example.jobservice.job.repository.JobRepository;
import com.example.jobservice.model.Job;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JobRecommendationService {

    private final CvClient cvClient;
    private final JobRepository jobRepository;

    public JobRecommendationService(CvClient cvClient, JobRepository jobRepository) {
        this.cvClient = cvClient;
        this.jobRepository = jobRepository;
    }

    public List<Job> getRecommendedJobsByUserId(Long userId) {

        System.out.println("[DEBUG] Calling NestJS CV service for userId: " + userId);

        CvDto cv;

        try {
            // 🔥 Feign call to NestJS: GET http://localhost:3000/cv/user/{userId}
            cv = cvClient.getCvByUserId(userId);
        } catch (FeignException.NotFound e) {
            // NestJS replied 404: no CV for this user
            System.out.println("[DEBUG] No CV found in NestJS for userId: " + userId);
            // 👉 choose behavior: empty list or all jobs
            return Collections.emptyList();
            // or: return jobRepository.findAll();
        }

        if (cv == null) {
            System.out.println("[DEBUG] CV is null for userId: " + userId);
            return Collections.emptyList();
        }

        System.out.println("[DEBUG] CV found for userId " + userId + " with headline: " + cv.getHeadline());

        // Simple recommendation: match job title with CV headline
        String headline = cv.getHeadline();
        if (headline == null || headline.isBlank()) {
            System.out.println("[DEBUG] No headline in CV -> returning all jobs.");
            return jobRepository.findAll();
        }

        List<Job> jobs =
                jobRepository.findByTitleContainingIgnoreCase(headline);

        System.out.println("[DEBUG] Recommended jobs found: " + jobs.size());

        return jobs;
    }
}
