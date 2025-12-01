package com.example.jobservice.service;

import com.example.jobservice.cv.DTO.CvDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CvPdfGenerator {

    public File generateCvPdf(CvDto cv) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        float margin = 50;
        float y = 750;

        // Title
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 18);
        content.newLineAtOffset(margin, y);
        content.showText("Curriculum Vitae");
        content.endText();

        y -= 30;

        // Helper to print label + value
        y = writeField(content, "Headline", cv.getHeadline(), margin, y);
        y = writeField(content, "Summary", cv.getSummary(), margin, y);
        y = writeField(content, "Personal info", cv.getPersonalInfo(), margin, y);
        y = writeField(content, "Technical skills", cv.getTechnicalSkills(), margin, y);
        y = writeField(content, "Soft skills", cv.getSoftSkills(), margin, y);
        y = writeField(content, "Work experience", cv.getWorkExperience(), margin, y);
        y = writeField(content, "Education", cv.getEducation(), margin, y);
        y = writeField(content, "Languages", cv.getLanguages(), margin, y);
        y = writeField(content, "Certifications", cv.getCertifications(), margin, y);
        y = writeField(content, "Projects", cv.getProjects(), margin, y);

        content.close();

        File tempFile = File.createTempFile("cv-", ".pdf");
        document.save(tempFile);
        document.close();

        return tempFile;
    }

    private float writeField(PDPageContentStream content,
                             String label,
                             String value,
                             float margin,
                             float startY) throws IOException {

        if (value == null || value.isBlank()) {
            return startY;
        }

        // Remove \r and split lines on \n
        String[] lines = value.replace("\r", "").split("\n");

        float y = startY;

        // Label
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.newLineAtOffset(margin, y);
        content.showText(label + ":");
        content.endText();

        y -= 15;

        // Each line of the field value
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 11);
            content.newLineAtOffset(margin + 15, y);
            content.showText(line);   // ✅ no '\n' inside now
            content.endText();
            y -= 14; // move down for next line
        }

        y -= 10; // extra spacing after the field block
        return y;
    }

}
