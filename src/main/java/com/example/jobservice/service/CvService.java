package com.example.jobservice.service;

import com.example.jobservice.cv.DTO.CvDto;
import com.example.jobservice.cv.client.CvClient;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CvService {

    private final CvClient cvClient;

    public CvService(CvClient cvClient) {
        this.cvClient = cvClient;
    }

    @CircuitBreaker(name = "cvServiceCB", fallbackMethod = "getCvFallback")
    public Optional<CvDto> getCvByUserId(Long userId) {
        try {
            CvDto cv = cvClient.getCvByUserId(userId);
            return Optional.of(cv);
        } catch (FeignException.NotFound e) {
            // cv-service returned 404 → user has no CV
            return Optional.empty();
        }
    }
    // Executed when circuit is OPEN or service fails
    public Optional<CvDto> getCvFallback(Long userId, Exception ex) {
        if (ex instanceof CallNotPermittedException) {
            System.out.println("[CB] cvServiceCB is OPEN -> call not permitted");
        } else {
            System.out.println("[CB] cvServiceCB fallback due to: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
        return Optional.empty();
    }
}
