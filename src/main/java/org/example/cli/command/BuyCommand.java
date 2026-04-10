package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Order;
import org.example.model.User;
import org.example.service.TicketService;

import java.util.ArrayList;
import java.util.List;

public class BuyCommand implements Command {
    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "buy --ticket sessionId:placeId [--ticket sessionId:placeId ...] – купить билеты (резерв)";
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
        FlagArgs flags = FlagArgs.parse(args, 1);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        List<String> ticketStrs = flags.all("--ticket");
        if (ticketStrs.isEmpty()) {
            ctx.getOut().println("Укажите хотя бы один билет через --ticket sessionId:placeId");
            return;
        }
        try {
            List<TicketService.TicketDto> dtos = new ArrayList<>();
            for (String ticketStr : ticketStrs) {
                String[] parts = ticketStr.split(":");
                if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
                    ctx.getOut().println("Неверный формат билета: " + ticketStr + ". Ожидается sessionId:placeId");
                    return;
                }
                int sessionId = Integer.parseInt(parts[0]);
                int placeId = Integer.parseInt(parts[1]);
                dtos.add(new TicketService.TicketDto(sessionId, placeId));
            }
            Order order = ctx.getTicketService().buyTickets(dtos);
            ctx.getOut().printf("Заказ #%d создан. Сумма: %s руб.\nОплатите в течение %d минут командой pay %d\n",
                    order.getId(), order.getAmount(), ctx.getTicketService().getReservationMinutes(), order.getId());
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат id.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}