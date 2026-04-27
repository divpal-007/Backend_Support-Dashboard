package com.support_dashboard.TellMe.util;

import org.springframework.stereotype.Component;

@Component
public class AIClassifier {
        public String classifyCategory(String text) {
            text = text.toLowerCase();

            if (text.contains("payment") || text.contains("refund"))
                return "Billing";

            if (text.contains("error") || text.contains("bug"))
                return "Technical";

            return "General";
        }

        public String classifyPriority(String text) {
            text = text.toLowerCase();

            if (text.contains("urgent") || text.contains("immediately"))
                return "High";

            if (text.contains("slow"))
                return "Medium";

            return "Low";
        }

        public String analyzeSentiment(String text) {
            text = text.toLowerCase();

            if (text.contains("bad") || text.contains("worst"))
                return "Negative";

            if (text.contains("good") || text.contains("great"))
                return "Positive";

            return "Neutral";
        }
    }
