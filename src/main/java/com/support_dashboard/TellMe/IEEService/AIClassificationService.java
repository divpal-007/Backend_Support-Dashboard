package com.support_dashboard.TellMe.IEEService;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.support_dashboard.TellMe.dto.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service //This class contains Business logic
public class AIClassificationService {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.url}")
    private String URL;


    private final RestTemplate restTemplate = new RestTemplate();
//    Spring HTTP Client (use to call APIs, send requests, receive response)
//          Java → external internet service



//    ------------------------ OLD LEGACY CODE FOR AI CLASSIFICATION-----------------
//    @Autowired
//    private ObjectMapper objectMapper;

//    public AIResponse analyze(String text) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String prompt = """
//        Analyze this support ticket.
//
//        Return ONLY valid JSON:
//        {
//          "category": "Billing | Technical | General",
//          "priority": "Low | Medium | High",
//          "sentiment": "Positive | Neutral | Negative"
//        }
//
//        Ticket: "%s"
//        """.formatted(text);
//
//        Map<String, Object> body = Map.of(
//                "contents", List.of(
//                        Map.of("parts", List.of(
//                                Map.of("text", prompt)
//                        ))
//                )
//        );
//
//        HttpEntity<Map<String, Object>> request =
//                new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response =
//                restTemplate.postForEntity(URL + apiKey, request, Map.class);
//
//        try {
//            List candidates = (List) response.getBody().get("candidates");
//            Map first = (Map) candidates.get(0);
//            Map content = (Map) first.get("content");
//            List parts = (List) content.get("parts");
//            Map part = (Map) parts.get(0);
//
//            String json = (String) part.get("text");
//            json= json.replace("```json", "").replace("```", "").trim();
//            System.out.println("newJson" +json);
//            return objectMapper.readValue(json, AIResponse.class);
//
//        } catch (Exception e) {
//            System.out.println("Exception Caused due to data error");
//            return fallback();
//        }
//    }
//     private AIResponse fallback() {
//        AIResponse res = new AIResponse();
//        System.out.println("in fallback method");
//        res.setCategory("General");
//        res.setPriority("Medium");
//        res.setSentiment("Neutral");
//        return res;
//    }
//------ OLD LEGACY CODE ENDS------------------------------------


    public String classify(String ticketTitle, String ticketDescription){
        String prompt = buildClassificationPrompt(ticketTitle, ticketDescription);
        return callAgent(prompt);
    }

    public String suggestResolution(String ticketTitle, String similarResolutions){
        String prompt = buildResolutionPrompt(ticketTitle, similarResolutions);
        return callAgent(prompt);
    }

//    Classification of the Ticket Based on AI Agent Suggestion

    private String buildClassificationPrompt(String ticketTitle, String ticketDescription){
        return """
                You are an IT operations ticket classifier for an INTELLIGENT IT OPERATIONS team.
                            Your ONLY job is to classify the ticket below.
                
                            STRICT RULES:
                            - Respond ONLY with valid JSON
                            - No explanation, no markdown, no extra text
                            - If ticket is unclear, use your best judgment — never say "I don't know"
                            - severity must be exactly one of: critical, high, medium, low
                            - category must be exactly one of: Infrastructure, Security, Payments, Performance, Application, Database, Network
                            - confidence must be a number between 0 and 100
                            - priority must be exactly one of: P1, P2, P3, P4
                
                            TICKET TITLE: %s
                            TICKET DESCRIPTION: %s
                
                            RESPOND WITH EXACTLY THIS JSON STRUCTURE — NOTHING ELSE:
                            {
                              "severity": "critical|high|medium|low",
                              "category": "Infrastructure|Security|Payments|Performance|Application|Database|Network",
                              "priority": "P1|P2|P3|P4",
                              "confidence": 0-100,
                              "reasoning": "one sentence max"
                            }
                """.formatted(ticketTitle, ticketDescription); //This injects variables into the text block in %s
    }

    private String buildResolutionPrompt(String ticketTitle, String similarResolutions){
        return """
                You are an INTELLIGENT IT OPERATIONS team resolution assistant.
                                         Your ONLY job is to suggest a resolution for the incident below.
                
                                         STRICT RULES:
                                         - Respond ONLY with valid JSON
                                         - No explanation, no markdown, no extra text
                                         - Base your suggestion on the similar past resolutions provided
                                         - Keep suggestion concise — maximum 3 steps
                                         - confidence must be a number between 0 and 100
                                         - If no similar resolutions exist, suggest based on general SRE knowledge
                
                                         CURRENT INCIDENT: %s
                
                                         SIMILAR PAST RESOLUTIONS:
                                         %s
                
                                         RESPOND WITH EXACTLY THIS JSON STRUCTURE — NOTHING ELSE:
                                         {
                                           "suggestedResolution": "step by step resolution",
                                           "confidence": 0-100,
                                           "basedOnSimilarIncidents": true|false
                                         }
                """.formatted(ticketTitle, similarResolutions);
    }

//    Gemini Agent Call
    private String callAgent(String prompt){
        String url = URL+apiKey;
        System.out.println("url:::"+url);
        Map<String,Object> requestBody = new HashMap<>();

        requestBody= Map.of("contents", List.of(Map.of(
                "parts",List.of(Map.of("text", prompt))
        )),
                "generationConfig", Map.of(
                        "temperature",0.1,
                        "maxOutputTokens",500,
                        "topP",0.8
                )
        );
        System.out.println("requestBody:::"+requestBody);
        Map response = restTemplate.postForObject(url, requestBody, Map.class);
        System.out.println("responseBody:::"+response);
//        extracting text from LLM response structure
        List candidates = (List) response.get("candidates");
        Map first = (Map) candidates.get(0);
        Map content = (Map) first.get("content");
        List parts =  (List) content.get("parts");
        Map part = (Map) parts.get(0);

        return (String) part.get("text");
    }
}
