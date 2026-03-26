// package com.ai.assistant.service;

// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import java.util.*;

// @Service
// public class AiService {

//     private final String API_KEY = "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";

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
// }


package com.ai.assistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    @Value("${openrouter.api.key}")
    private String API_KEY;

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
            body.put("model", "openai/gpt-4o-mini");

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
            e.printStackTrace();
            return "AI Error: " + e.getMessage();
        }
    }
       public String analyzeFile(String content) {

    // limit size (VERY IMPORTANT)
    if(content.length() > 8000){
        content = content.substring(0, 8000);
    }

    return askAI(
        "Analyze the following file and explain clearly:\n\n" + content
    );
}
}