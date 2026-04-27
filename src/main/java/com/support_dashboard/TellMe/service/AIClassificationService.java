package com.support_dashboard.TellMe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.support_dashboard.TellMe.dto.AIResponse;
import com.support_dashboard.TellMe.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AIClassificationService {

    @Value("${google.api.key}")
    private String apiKey;

    private final String URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    @Autowired
    private ObjectMapper objectMapper;

    public AIResponse analyze(String text) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = """
        Analyze this support ticket.

        Return ONLY valid JSON:
        {
          "category": "Billing | Technical | General",
          "priority": "Low | Medium | High",
          "sentiment": "Positive | Neutral | Negative"
        }

        Ticket: "%s"
        """.formatted(text);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(URL + apiKey, request, Map.class);

        try {
            List candidates = (List) response.getBody().get("candidates");
            Map first = (Map) candidates.get(0);
            Map content = (Map) first.get("content");
            List parts = (List) content.get("parts");
            Map part = (Map) parts.get(0);

            String json = (String) part.get("text");
            json= json.replace("```json", "").replace("```", "").trim();
            System.out.println("newJson" +json);
            return objectMapper.readValue(json, AIResponse.class);

        } catch (Exception e) {
            System.out.println("Exception Caused due to data error");
            return fallback();
        }
    }

    private AIResponse fallback() {
        AIResponse res = new AIResponse();
        System.out.println("in fallback method");
        res.setCategory("General");
        res.setPriority("Medium");
        res.setSentiment("Neutral");
        return res;
    }
}
