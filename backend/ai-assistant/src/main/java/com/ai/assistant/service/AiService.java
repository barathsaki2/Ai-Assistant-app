
// package com.ai.assistant.service;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import java.util.*;

// @Service
// public class AiService {

//     @Value("${openrouter.api.key}")
//     private String API_KEY;

//     public String askAI(String question) {

//         try {

//             RestTemplate restTemplate = new RestTemplate();

//             String url = "https://openrouter.ai/api/v1/chat/completions";

//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_JSON);
//             headers.setBearerAuth(API_KEY);

//             headers.add("HTTP-Referer", "http://localhost:3000");
//             headers.add("X-Title", "AI Assistant");

//             Map<String, Object> body = new HashMap<>();

//             // ✅ FIXED MODEL
//             body.put("model", "openrouter/auto");

//             List<Map<String, String>> messages = new ArrayList<>();

//             Map<String, String> message = new HashMap<>();
//             message.put("role", "user");
//             message.put("content", question);

//             messages.add(message);
//             body.put("messages", messages);

//             HttpEntity<Map<String, Object>> request =
//                     new HttpEntity<>(body, headers);

//             ResponseEntity<Map> response =
//                     restTemplate.postForEntity(url, request, Map.class);

//             Map responseBody = response.getBody();

//             List choices = (List) responseBody.get("choices");
//             Map firstChoice = (Map) choices.get(0);
//             Map msg = (Map) firstChoice.get("message");

//             return msg.get("content").toString();

//         } catch (Exception e) {
//             e.printStackTrace();
//             return "AI Error: " + e.getMessage();
//         }
//     }

//     public String analyzeFile(String content) {

//         if(content.length() > 8000){
//             content = content.substring(0, 8000);
//         }

//         return askAI(
//             "Analyze the following file and explain clearly:\n\n" + content
//         );
//     }
// }

package com.ai.assistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import net.sourceforge.tess4j.Tesseract;

@Service
public class AiService {

    @Value("${openrouter.api.key}")
    private String API_KEY;

    // ================= AI CHAT =================
    public String askAI(String question) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = "https://openrouter.ai/api/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            headers.add("HTTP-Referer", "http://localhost:3000");
            headers.add("X-Title", "AI Assistant");

            Map<String, Object> body = new HashMap<>();
            body.put("model", "openrouter/auto");

            List<Map<String, String>> messages = new ArrayList<>();

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", question);

            messages.add(message);
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            Map responseBody = response.getBody();

            List choices = (List) responseBody.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map msg = (Map) firstChoice.get("message");

            return msg.get("content").toString();

        } catch (Exception e) {
            return "AI Error: " + e.getMessage();
        }
    }

    // ================= FILE ANALYZE =================

   public String analyzeFile(MultipartFile file) {

    try {

        String fileName = file.getOriginalFilename().toLowerCase();
        String contentType = file.getContentType();

        String text = "";

        // ---------- PDF ----------
        if (fileName.endsWith(".pdf")) {
            text = extractPdf(file);
        }

        // ---------- WORD ----------
        else if (fileName.endsWith(".docx")) {
            text = extractWord(file);
        }

        // ---------- IMAGE ----------
        else if (contentType != null && contentType.startsWith("image")) {
            text = extractImage(file);
        }

        // ---------- TEXT ----------
        else {
            text = new String(file.getBytes());
        }

        if (text.length() > 8000) {
            text = text.substring(0, 8000);
        }

        return askAI("Analyze this file:\n\n" + text);

    } catch (Exception e) {
        e.printStackTrace();
        return "File Analyze Error: " + e.getMessage();
    }
}

    // ================= PDF =================
    private String extractPdf(MultipartFile file) throws Exception {

        PDDocument document =
                PDDocument.load(file.getInputStream());

        PDFTextStripper stripper =
                new PDFTextStripper();

        String text = stripper.getText(document);

        document.close();

        return text;
    }

    // ================= WORD =================
    private String extractWord(MultipartFile file) throws Exception {

        XWPFDocument doc =
                new XWPFDocument(file.getInputStream());

        XWPFWordExtractor extractor =
                new XWPFWordExtractor(doc);

        return extractor.getText();
    }

    // ================= IMAGE OCR =================
 private String extractImage(MultipartFile file) throws Exception {

    File temp = File.createTempFile("upload", file.getOriginalFilename());
    file.transferTo(temp);

    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

    String text = tesseract.doOCR(temp);

    temp.delete();

    return text;
}
}