package com.example.jobservice.controller;

import com.example.jobservice.model.Job;
import com.example.jobservice.service.JobRecommendationService;
import com.example.jobservice.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobRecommendationService jobService;
    private final JwtUtils jwtUtils;

    public JobController(JobRecommendationService jobService, JwtUtils jwtUtils) {
        this.jobService = jobService;
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

        List<Job> jobs = jobService.getRecommendedJobsByUserId(userId);

        return ResponseEntity.ok(jobs);
    }
}
