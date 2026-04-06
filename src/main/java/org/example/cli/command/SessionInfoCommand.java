package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Film;
import org.example.model.Hall;
import org.example.model.Session;

import java.math.BigDecimal;

public class SessionInfoCommand implements Command {
    @Override
    public String getName() {
        return "session info";
    }

    @Override
    public String getDescription() {
        return "session info <id> – показать информацию о сеансе";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: session info <id>");
            return;
        }
        try {
            int sessionId = Integer.parseInt(args[2]);
            Session session = ctx.getSessionService().getById(sessionId);
            if (session == null) {
                ctx.getOut().println("Сеанс с id " + sessionId + " не найден.");
                return;
            }
            Film film = ctx.getFilmService().getById(session.getFilmId());
            Hall hall = ctx.getHallService().getById(session.getHallId());
            if (film == null || hall == null) {
                ctx.getOut().println("Ошибка: фильм или зал не найдены.");
                return;
            }
            BigDecimal basePrice = film.getPrice().add(hall.getPrice().multiply(BigDecimal.valueOf(0.5)));
            ctx.getOut().println("Информация о сеансе:");
            ctx.getOut().printf("  Id сеанса: %d\n  Фильм: %s\n  Зал: %d\n  Начало: %s\n  Базовая цена билета: %s руб.\n",
                    session.getId(), film.getName(), hall.getId(), session.getStartTime(), basePrice);
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        }
    }
}