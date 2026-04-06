package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Hall;
import org.example.model.Place;
import org.example.model.Ticket;
import org.example.model.TicketStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminTicketListCommand implements Command {
    @Override
    public String getName() {
        return "ticket list";
    }

    @Override
    public String getDescription() {
        return "ticket list [--session <id>] [--user <id>] [--status <status>] – список билетов (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }

        String sessionIdStr = flags.optional("--session");
        String userIdStr = flags.optional("--user");
        String statusStr = flags.optional("--status");

        try {
            List<Ticket> tickets = null;

            // 1. Применяем фильтры в порядке приоритета
            if (sessionIdStr != null) {
                int sessionId = Integer.parseInt(sessionIdStr);
                if (statusStr != null) {
                    TicketStatus status = TicketStatus.valueOf(statusStr.toUpperCase());
                    tickets = ctx.getTicketService().findBySessionIdAndStatus(sessionId, status);
                } else {
                    tickets = ctx.getTicketService().findBySessionId(sessionId);
                }
            } else if (statusStr != null) {
                TicketStatus status = TicketStatus.valueOf(statusStr.toUpperCase());
                tickets = ctx.getTicketService().findByStatus(status);
            } else if (userIdStr != null) {
                int userId = Integer.parseInt(userIdStr);
                tickets = ctx.getTicketService().findByUserId(userId);
            } else {
                tickets = ctx.getTicketService().getAll();
            }

            // 2. Если указан userId и билеты уже получены (не из фильтра по userId), пересекаем списки
            if (userIdStr != null && tickets != null && !tickets.isEmpty()) {
                int userId = Integer.parseInt(userIdStr);
                Set<Integer> userTicketIds = ctx.getTicketService().findByUserId(userId).stream()
                        .map(Ticket::getId)
                        .collect(Collectors.toSet());
                tickets = tickets.stream()
                        .filter(t -> userTicketIds.contains(t.getId()))
                        .collect(Collectors.toList());
            } else if (userIdStr != null && (tickets == null || tickets.isEmpty())) {
                // Если по другим фильтрам ничего не нашли, но указан userId – берём билеты пользователя
                int userId = Integer.parseInt(userIdStr);
                tickets = ctx.getTicketService().findByUserId(userId);
            }

            if (tickets == null || tickets.isEmpty()) {
                ctx.getOut().println("Билеты не найдены.");
                return;
            }

            ctx.getOut().println("Список билетов:");
            for (Ticket t : tickets) {
                ctx.getOut().printf("id: %d, статус: %s, цена: %s, заказ: %d, сеанс: %d, место: %d%n",
                        t.getId(), t.getTicketStatus(), t.getPrice(), t.getOrdersId(), t.getSessionId(), t.getPlaceId());
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат id.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Неверный статус. Допустимые: RESERVED, SOLD, USED, CANCELED");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}
