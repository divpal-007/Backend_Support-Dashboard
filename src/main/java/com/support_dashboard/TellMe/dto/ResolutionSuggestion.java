package com.support_dashboard.TellMe.dto;

public class ResolutionSuggestion {

    private String suggestedResolution; //step by step resolution suggestion

    private Integer confidence;

    // true - based on our past resolved tickets
    // false - based on gemini general knowledge
    private boolean basedOnSimilarIncidents;

    private boolean aiClassified = false;

    public String getSuggestedResolution() { return suggestedResolution; }
    public void setSuggestedResolution(String suggestedResolution) {
        this.suggestedResolution = suggestedResolution;
    }

    public Integer getConfidence() { return confidence; }
    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public boolean isBasedOnSimilarIncidents() {return basedOnSimilarIncidents; }
    public void setBasedOnSimilarIncidents(boolean basedOnSimilarIncidents) {this.basedOnSimilarIncidents = basedOnSimilarIncidents;}

    public boolean isAiClassified() { return aiClassified; }
    public void setAiClassified(boolean aiClassified) {this.aiClassified = aiClassified;}

    // safe default when IEE has no data or LLM fails
    public static ResolutionSuggestion defaultSuggestion() {
        ResolutionSuggestion suggestion = new ResolutionSuggestion();
        suggestion.setSuggestedResolution("No Similar Incidents found. Manual Investigation Required");
        suggestion.setConfidence(0);
        suggestion.setBasedOnSimilarIncidents(false);
        suggestion.setAiClassified(false);

        return suggestion;
    }
}
