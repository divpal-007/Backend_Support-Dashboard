package com.support_dashboard.TellMe.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
//👉 Registers this class as a bean
//
//        Spring manages it:
//
//Creates object
//Injects where needed
public class HealthService {
    public Map<String,String> getStatus () {
        return Map.of(
                "status","UP",
                "service", "Operix Dashboard",
                "version","3.0.0"
        );
    }
}
