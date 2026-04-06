package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminSessionCreateCommand implements Command {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String getName() {
        return "session create";
    }

    @Override
    public String getDescription() {
        return "session create --hall <id> --film <id> --start \"yyyy-MM-dd HH:mm\" – создать сеанс (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 8) {
            ctx.getOut().println("Использование: session create --hall <id> --film <id> --start \"yyyy-MM-dd HH:mm\"");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String hallIdStr = flags.require("--hall");
        String filmIdStr = flags.require("--film");
        String startStr = flags.require("--start");
        if (hallIdStr == null || filmIdStr == null || startStr == null) return;
        try {
            int hallId = Integer.parseInt(hallIdStr);
            int filmId = Integer.parseInt(filmIdStr);
            LocalDateTime startTime = LocalDateTime.parse(startStr, FORMATTER);
            ctx.getSessionService().createSession(hallId, filmId, startTime);
            ctx.getOut().println("Сеанс создан.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}