package org.pqkkkkk.ticsys.event_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "ticsys_event_service_ticket_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ticketId;

    @Column(name = "ticket_name", nullable = false, length = 30)
    String ticketName;

    @Column(name = "ticket_service", length = 300)
    String ticketService;

    @Column(name = "ticket_price", nullable = false)
    Double ticketPrice;

    @Column(name = "ticket_total_quantity", nullable = false)
    @Min(value = 1, message = "Ticket total quantity must be at least 1")
    Integer ticketTotalQuantity;

    @Column(name = "ticket_stock_quantity", nullable = false)
    @Min(value = 0, message = "Ticket stock quantity must be at least 0")
    Integer ticketStockQuantity;

    @Column(name = "ticket_max_qty_per_order")
    @Builder.Default
    @Min(value = 1, message = "Max quantity per order must be at least 1")
    Integer maxQtyPerOrder = 3;

    @Column(name = "ticket_sold_start_time")
    LocalDateTime soldStartTime;

    @Column(name = "ticket_sold_end_time")
    LocalDateTime soldEndTime;

    @ManyToOne
    @JoinColumn(name = "event_date_id", nullable = false)
    EventDate eventDate;

    

}
