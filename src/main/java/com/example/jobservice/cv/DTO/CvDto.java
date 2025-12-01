package com.example.jobservice.cv.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CvDto {

    @JsonProperty("user_id")
    private Long userId;

    private String headline;
    @JsonProperty("personal_info")
    private String personalInfo;
    @JsonProperty("technical_skills")
    private String technicalSkills;
    @JsonProperty("soft_skills")
    private String softSkills;
    @JsonProperty("work_experience")
    private String workExperience;
    private String education;
    private String languages;
    private String certifications;
    private String projects;
    private String summary;

    // we can keep it but we won't use it now
    @JsonProperty("pdf_url")
    private String pdfUrl;

    // getters & setters …

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getPersonalInfo() { return personalInfo; }
    public void setPersonalInfo(String personalInfo) { this.personalInfo = personalInfo; }

    public String getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(String technicalSkills) { this.technicalSkills = technicalSkills; }

    public String getSoftSkills() { return softSkills; }
    public void setSoftSkills(String softSkills) { this.softSkills = softSkills; }

    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }

    public String getProjects() { return projects; }
    public void setProjects(String projects) { this.projects = projects; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
}
