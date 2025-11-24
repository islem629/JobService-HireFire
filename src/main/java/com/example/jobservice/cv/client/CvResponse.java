package com.example.jobservice.cv.client;

import lombok.Data;

@Data
public class CvResponse {
    private Long id;
    private Long user_id;
    private String headline;
    private String personal_info;
    private String technical_skills;
    private String soft_skills;
    private String work_experience;
    private String education;
    private String languages;
    private String certifications;
    private String projects;
    private String summary;
    private String created_at;
    private String updated_at;
}
