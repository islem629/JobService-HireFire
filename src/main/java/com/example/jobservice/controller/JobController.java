package com.example.jobservice.controller;

import com.example.jobservice.model.Job;
import com.example.jobservice.security.JwtUtils;
import com.example.jobservice.service.JobApplicationService;
import com.example.jobservice.service.JobRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobRecommendationService jobRecommendationService;
    private final JobApplicationService jobApplicationService;
    private final JwtUtils jwtUtils;

    public JobController(JobRecommendationService jobRecommendationService,
                         JobApplicationService jobApplicationService,
                         JwtUtils jwtUtils) {
        this.jobRecommendationService = jobRecommendationService;
        this.jobApplicationService = jobApplicationService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Job>> getRecommendedJobs(@RequestHeader("Authorization") String token) {
        System.out.println("[DEBUG] JWT token received: " + token);

        String jwt = token.replace("Bearer ", "");
        Map<String, Object> claims = jwtUtils.extractAllClaims(jwt);
        System.out.println("[DEBUG] Extracted claims: " + claims);

        Long userId = ((Number) claims.get("userId")).longValue();
        System.out.println("[DEBUG] Extracted userId from JWT: " + userId);

        List<Job> jobs = jobRecommendationService.getRecommendedJobsByUserId(userId);

        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<Void> applyToJob(
            @PathVariable Long jobId,
            @RequestHeader("Authorization") String token) {

        System.out.println("[DEBUG] applyToJob called for jobId = " + jobId);

        String jwt = token.replace("Bearer ", "");
        Map<String, Object> claims = jwtUtils.extractAllClaims(jwt);

        Long userId = ((Number) claims.get("userId")).longValue();
        String userEmail = (String) claims.get("email"); // email must be present in JWT

        System.out.println("[DEBUG] userId = " + userId + ", email = " + userEmail);

        jobApplicationService.applyToJob(jobId, userId, userEmail);

        return ResponseEntity.ok().build();
    }
    // TEMPORARY TEST ENDPOINT (no JWT, no headers)
    @PostMapping("/{jobId}/apply/test")
    public ResponseEntity<Void> applyToJobTest(@PathVariable Long jobId) {
        // ✅ Hardcoded test user
        Long userId = 3L; // put an existing userId that has a CV in cv-service
        String userEmail = "islem@gmail.com"; // any email for now

        jobApplicationService.applyToJob(jobId, userId, userEmail);

        return ResponseEntity.ok().build();
    }

}
