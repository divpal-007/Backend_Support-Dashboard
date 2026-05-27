package com.support_dashboard.TellMe.controller;

import com.support_dashboard.TellMe.IEEService.IEEActiveLearning;
import com.support_dashboard.TellMe.dto.CreateEscalationRequest;
import com.support_dashboard.TellMe.model.Escalation;
import com.support_dashboard.TellMe.service.EscalationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/escalations")
public class EscalationController {

    private final EscalationService escalationService;
    private final IEEActiveLearning ieeeActiveLearning;

    public EscalationController(IEEActiveLearning ieeeActiveLearning, EscalationService escalationService) {
        this.ieeeActiveLearning = ieeeActiveLearning;
        this.escalationService = escalationService;
    }

    //Creation of new Ticket
    //LLM Classifies Automatically
    //IEE suggests resolution automatically
    @PostMapping
    public ResponseEntity<Escalation> create(@Valid @RequestBody CreateEscalationRequest request){
        return ResponseEntity.ok(escalationService.createEscalation(request));
    }

    @GetMapping
    public ResponseEntity<List<Escalation>> getAllEscalations(){
        return ResponseEntity.ok(escalationService.getAllEscalations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Escalation> getEscalationById(@PathVariable Long id){
        return ResponseEntity.ok(escalationService.getEscalationById(id));
    }

//    Ticket Resolution - IEE Engine learning continues
//    Resolution Fix is captured here for future references
//    The Resolution description helps IEE to grow and get mature - over the time this will the benefit for less token consumption
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long id, @RequestBody Map<String,String> body){
        ieeeActiveLearning.recordResolution(id,body.get("resolution"));
        return ResponseEntity.ok().build();
    }
}
