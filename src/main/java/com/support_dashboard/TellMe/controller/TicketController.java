package com.support_dashboard.TellMe.controller;

import com.support_dashboard.TellMe.model.Ticket;
import com.support_dashboard.TellMe.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return service.createTicket(ticket);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return service.getAllTickets();
    }

    @PutMapping("/{id}")
    public Ticket update(@PathVariable Long id, @RequestBody Ticket ticket) {
        return service.updateTicket(id, ticket);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTicket(id);
    }
}