package com.support_dashboard.TellMe.controller;

import com.support_dashboard.TellMe.IEEService.IEEActiveLearning;
import com.support_dashboard.TellMe.dto.DashboardSummary;
import com.support_dashboard.TellMe.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

     private final DashboardService dashboardService;
     private final IEEActiveLearning iEEActiveLearning;

     public DashboardController(IEEActiveLearning iEEActiveLearning,DashboardService dashboardService) {
         this.dashboardService = dashboardService;
         this.iEEActiveLearning = iEEActiveLearning;
     }

     // main dashboard data
     @GetMapping("/summary")
     public ResponseEntity<DashboardSummary> getDashboardSummary(){
         return ResponseEntity.ok(dashboardService.getDashboardSummary());
     }

     // IEE engine status - called by Sidebar
     // drives the Active/Learning dots
     @GetMapping("/iee-status")
     public ResponseEntity<Map<String,Object>> getIeeeStatus(){
         return ResponseEntity.ok(iEEActiveLearning.getStatus());
     }
}
