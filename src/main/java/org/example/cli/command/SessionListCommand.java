package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Film;
import org.example.model.Hall;
import org.example.model.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SessionListCommand implements Command {
    @Override
    public String getName() {
        return "session list";
    }

    @Override
    public String getDescription() {
        return "session list [--film <id>] [--date YYYY-MM-DD] – показать сеансы";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        List<Session> sessions;
        String filmIdStr = null;
        String dateStr = null;
        for (int i = 2; i < args.length; i++) {
            if ("--film".equals(args[i]) && i + 1 < args.length) filmIdStr = args[++i];
            else if ("--date".equals(args[i]) && i + 1 < args.length) dateStr = args[++i];
        }

        if (filmIdStr != null && dateStr != null) {
            int filmId = Integer.parseInt(filmIdStr);
            LocalDate date = LocalDate.parse(dateStr);
            LocalDateTime from = date.atStartOfDay();
            LocalDateTime to = date.plusDays(1).atStartOfDay();
            sessions = ctx.getSessionService().findByFilmIdAndDateRange(filmId, from, to);
        } else if (filmIdStr != null) {
            sessions = ctx.getSessionService().findByFilmId(Integer.parseInt(filmIdStr));
        } else if (dateStr != null) {
            LocalDate date = LocalDate.parse(dateStr);
            sessions = ctx.getSessionService().findByStartTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        } else {
            sessions = ctx.getSessionService().findUpcoming(LocalDateTime.now());
        }
        if (sessions.isEmpty()) {
            ctx.getOut().println("Сеансы не найдены.");
            return;
        }
        ctx.getOut().println("Сеансы:");
        for (Session s : sessions) {
            Film film = ctx.getFilmService().getById(s.getFilmId());
            Hall hall = ctx.getHallService().getById(s.getHallId());
            ctx.getOut().printf("  %d. Фильм: %s, Зал: %s, Начало: %s\n",
                    s.getId(), film.getName(), hall.getId(), s.getStartTime());
        }
    }
}