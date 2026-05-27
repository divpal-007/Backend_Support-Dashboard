package com.support_dashboard.TellMe.service;

import com.support_dashboard.TellMe.IEEService.AIClassificationService;
import com.support_dashboard.TellMe.IEEService.HallucinationDetection;
import com.support_dashboard.TellMe.IEEService.IEEActiveLearning;
import com.support_dashboard.TellMe.dto.AgentResponseResult;
import com.support_dashboard.TellMe.dto.CreateEscalationRequest;
import com.support_dashboard.TellMe.dto.ResolutionSuggestion;
import com.support_dashboard.TellMe.model.Escalation;
import com.support_dashboard.TellMe.repository.EscalationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationService {

    private final EscalationRepository escalationRepository;
    private final HallucinationDetection hallucinationDetection;
    private final IEEActiveLearning ieeActiveLearning;

    public EscalationService(EscalationRepository escalationRepository,HallucinationDetection hallucinationDetection,IEEActiveLearning ieeActiveLearning) {
        this.escalationRepository = escalationRepository;
        this.hallucinationDetection = hallucinationDetection;
        this.ieeActiveLearning = ieeActiveLearning;
    }
    public Escalation createEscalation(CreateEscalationRequest request){

        //Step 1- Gemini Classifies the ticket and prevent Hallucination
        AgentResponseResult classification = hallucinationDetection.detectHallucination(request.getTitle(), request.getDescription());

        //Step 2- IEE suggests Resolution
                // searches past resolved tickets if they are available
        ResolutionSuggestion suggestion = ieeActiveLearning.suggestResolution(request.getTitle(), classification.getCategory());

        //Step 3- Build and Save escalation
        Escalation escalation = getEscalation(request, classification, suggestion);

        return escalationRepository.save(escalation);
    }

    private static Escalation getEscalation(CreateEscalationRequest request, AgentResponseResult classification, ResolutionSuggestion suggestion) {
        Escalation escalation = new Escalation();
        escalation.setTitle(request.getTitle());
        escalation.setDescription(request.getDescription());
        escalation.setCreatedBy(request.getCreatedBy());

        //AI classification Result
        escalation.setSeverity(classification.getSeverity());
        escalation.setCategory(classification.getCategory());
        escalation.setPriority(classification.getPriority());
        escalation.setAiConfidence(classification.getConfidence());
        escalation.setAiReasoning(classification.getReasoning());
        escalation.setAiClassified(true);

        //IEE Resolution Suggestion
        escalation.setSuggestedResolution(suggestion.getSuggestedResolution());
        escalation.setBasedOnSimilar(suggestion.isBasedOnSimilarIncidents());
        escalation.setStatus("open");

        return escalation;
    }

    // get all escalations - for escalation page
    public List<Escalation> getAllEscalations(){
        return escalationRepository.findByStatusOrderByCreatedAtDesc("open");
    }

    // get Single escalation by id
    public Escalation getEscalationById(Long id){
        return escalationRepository.findById(id).orElseThrow(() -> new RuntimeException("Escalation not found: " + id));
    }
}
