package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Place;
import org.example.model.Ticket;
import org.example.model.User;

import java.util.List;

public class MyTicketsCommand implements Command {
    @Override
    public String getName() {
        return "my tickets";
    }

    @Override
    public String getDescription() {
        return "my tickets – показать мои билеты";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        User user = ctx.getCurrentUser();
        if (user == null) {
            ctx.getOut().println("Необходимо войти в систему.");
            return;
        }
        List<Ticket> tickets = ctx.getTicketService().findByUserId(user.getId());
        if (tickets.isEmpty()) {
            ctx.getOut().println("У вас нет билетов.");
            return;
        }
        ctx.getOut().println("Ваши билеты:");
        Place p;
        for (Ticket t : tickets) {
            p = ctx.getPlaceService().getById(t.getPlaceId());
            ctx.getOut().printf("  Билет #%d, сеанс #%d, ряд #%d, место #%d, цена: %s, статус: %s%n",
                    t.getId(), t.getSessionId(), p.getRows(), p.getSeat(), t.getPrice(), t.getTicketStatus());
        }
    }
}