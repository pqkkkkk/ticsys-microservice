package org.pqkkkkk.ticsys.event_service.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.pqkkkkk.ticsys.event_service.model.Ticket;

public class TicketRowMapper implements RowMapper<Ticket> {

    @Override
    @Nullable
    public Ticket mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setEventId(rs.getInt("eventId"));
        ticket.setPrice(rs.getInt("price"));
        ticket.setQuantity(rs.getInt("quantity"));
        ticket.setService(rs.getString("service"));
        ticket.setName(rs.getString("name"));
        ticket.setMinQtyInOrder(rs.getInt("minQtyInOrder"));
        ticket.setMaxQtyInOrder(rs.getInt("maxQtyInOrder"));
        return ticket;
    }

}
