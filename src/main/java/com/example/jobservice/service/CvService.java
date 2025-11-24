package com.example.jobservice.service;

import com.example.jobservice.cv.DTO.CvDto;
import com.example.jobservice.cv.client.CvClient;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CvService {

    private final CvClient cvClient;

    public CvService(CvClient cvClient) {
        this.cvClient = cvClient;
    }

    /**
     * Get CV by userId from cv-service.
     * Returns Optional.empty() if no CV exists (404 from cv-service).
     */
    public Optional<CvDto> getCvByUserId(Long userId) {
        try {
            CvDto cv = cvClient.getCvByUserId(userId);
            return Optional.of(cv);
        } catch (FeignException.NotFound e) {
            // cv-service returned 404 → user has no CV
            return Optional.empty();
        }
    }
}
