package com.support_dashboard.TellMe.repository;

import com.support_dashboard.TellMe.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
