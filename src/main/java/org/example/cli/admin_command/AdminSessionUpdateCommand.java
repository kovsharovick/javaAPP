package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Film;
import org.example.model.Session;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminSessionUpdateCommand implements Command {
    @Override
    public String getName() {
        return "session update";
    }

    @Override
    public String getDescription() {
        return "session update --id <id> --hall <id> --film <id> --start \"yyyy-MM-dd HH:mm\" – обновление сеанса (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 9) {
            ctx.getOut().println("Использование: session update --id <id> --hall <id> --film <id> --start \"yyyy-MM-dd HH:mm\"");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String idStr = flags.require("--id");
        String hallIdStr = flags.optional("--hall");
        String filmIdStr = flags.optional("--film");
        String startStr = flags.optional("--start");
        if (idStr == null) {
            ctx.getOut().println("Не указан --id");
            return;
        }
        try {
            int sessionId = Integer.parseInt(idStr);
            Session session = ctx.getSessionService().getById(sessionId);
            if (session == null) {
                ctx.getOut().println("Сеанс не найден.");
                return;
            }
            if (hallIdStr != null) session.setHallId(Integer.parseInt(hallIdStr));
            if (filmIdStr != null) {
                int filmId = Integer.parseInt(filmIdStr);
                Film film = ctx.getFilmService().getById(filmId);
                if (film == null) {
                    ctx.getOut().println("Фильм с id " + filmId + " не найден.");
                    return;
                }
                session.setFilmId(filmId);
            }
            if (startStr != null) {
                LocalDateTime newStart = LocalDateTime.parse(startStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                session.setStartTime(newStart);
                Film film = ctx.getFilmService().getById(session.getFilmId());
                if (film != null) {
                    session.setFinishTime(newStart.plus(film.getDuration()));
                }
            }
            if (ctx.getSessionService().existsOverlap(session.getHallId(), session.getStartTime(), session.getFinishTime(), session.getId())) {
                ctx.getOut().println("Новое время конфликтует с другим сеансом.");
                return;
            }
            ctx.getSessionService().updateSession(session);
            ctx.getOut().println("Сеанс обновлён.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат числа.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}
