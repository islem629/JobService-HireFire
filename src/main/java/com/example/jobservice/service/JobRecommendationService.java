package com.example.jobservice.service;

import com.example.jobservice.cv.client.CvClient;
import com.example.jobservice.cv.DTO.CvDto;
import com.example.jobservice.job.repository.JobRepository;
import com.example.jobservice.model.Job;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            cv = cvClient.getCvByUserId(userId);
        } catch (FeignException.NotFound e) {
            System.out.println("[DEBUG] No CV found in NestJS for userId: " + userId);
            return Collections.emptyList();
        }

        if (cv == null) {
            System.out.println("[DEBUG] CV is null for userId: " + userId);
            return Collections.emptyList();
        }

        System.out.println("[DEBUG] CV found for userId " + userId + " with headline: " + cv.getHeadline());

        String headline = cv.getHeadline();
        List<Job> jobs;

        if (headline == null || headline.isBlank()) {
            System.out.println("[DEBUG] No headline in CV -> using all jobs.");
            jobs = jobRepository.findAll();
        } else {
            jobs = jobRepository.findByTitleContainingIgnoreCase(headline);
        }

        // 🔎 filter only valid jobs based on dates
        LocalDate today = LocalDate.now();
        List<Job> validJobs = jobs.stream()
                .filter(job -> isJobValid(job, today))
                .toList();

        System.out.println("[DEBUG] Recommended valid jobs found: " + validJobs.size());

        return validJobs;
    }

    private boolean isJobValid(Job job, LocalDate today) {
        LocalDate start = job.getDateDebut();
        LocalDate end   = job.getDateExpired();

        boolean notBeforeStart = (start == null || !start.isAfter(today));   // start <= today
        boolean notAfterEnd    = (end == null   || !end.isBefore(today));    // end >= today

        return notBeforeStart && notAfterEnd;
    }
}
