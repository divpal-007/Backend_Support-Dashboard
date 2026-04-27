package com.support_dashboard.TellMe.service;

import com.support_dashboard.TellMe.dto.AIResponse;
import com.support_dashboard.TellMe.model.Ticket;
import com.support_dashboard.TellMe.repository.TicketRepository;
//import com.support_dashboard.TellMe.util.AIClassifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repo;
    private final AIClassificationService acs;

    public TicketService(TicketRepository repo, AIClassificationService ai) {
        this.repo = repo;
        this.acs = ai;
    }

    public Ticket createTicket(Ticket ticket) {
        try {
            AIResponse ai = acs.analyze(ticket.getDescription());
            ticket.setCategory(ai.getCategory());
            ticket.setPriority(ai.getPriority());
            ticket.setSentiment(ai.getSentiment());
        }catch(Exception e){
            System.out.println("Exception recorded in creating ticket"+e);
            ticket.setCategory("General");
            ticket.setPriority("Medium");
            ticket.setSentiment("Neutral");
            }
        return repo.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return repo.findAll();
    }

    public Ticket updateTicket(Long id, Ticket updated) {
        Ticket existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        System.out.println("inside update ticket");
          existing.setStatus(updated.getStatus());

        return repo.save(existing);
    }

    public void deleteTicket(Long id) {
        repo.deleteById(id);
    }
}
