package com.support_dashboard.TellMe.controller;

import com.support_dashboard.TellMe.service.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController           // @RestController - 1) This class handles HTTP requests
                            //2) Return value → automatically converted to JSON
@RequestMapping("/api")
public class HealthController {
//    This is our Controller layer
//
//    Responsibility:
//
//    Accept request
//    Send response
//
//👉 It should NOT contain business logic
//👉 Sets a base URL
//
//    So all endpoints become:
//
//            /api/health

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService= healthService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(healthService.getStatus());
    }
}
