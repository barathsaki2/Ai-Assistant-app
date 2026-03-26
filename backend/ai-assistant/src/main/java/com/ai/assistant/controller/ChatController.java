package com.ai.assistant.controller;

import com.ai.assistant.service.AiService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final AiService aiService;

    public ChatController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping
    public String test() {
        return "AI Assistant Running 🚀";
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        return aiService.askAI(q);
    }
    @PostMapping("/analyze")
public String analyze(@RequestBody Map<String,String> body){
    return aiService.analyzeFile(body.get("content"));
}
}