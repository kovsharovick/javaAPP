package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Film;

import java.math.BigDecimal;
import java.time.Duration;

public class AdminFilmUpdateCommand implements Command {
    @Override
    public String getName() {
        return "film update";
    }

    @Override
    public String getDescription() {
        return "film update --id <id> [--name <name>] [--duration <min>] [--price <price>] [--description <text>] [--poster-url <url>] – обновить фильм (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 4) {
            ctx.getOut().println("Использование: film update --id <id> [--name <name>] [--duration <min>] [--price <price>] [--description <text>] [--poster-url <url>]");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String idStr = flags.require("--id");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Film film = ctx.getFilmService().getById(id);
            if (film == null) {
                ctx.getOut().println("Фильм с id " + id + " не найден.");
                return;
            }
            String name = flags.optional("--name");
            if (name != null) film.setName(name);
            String durationStr = flags.optional("--duration");
            if (durationStr != null) film.setDuration(Duration.ofMinutes(Integer.parseInt(durationStr)));
            String priceStr = flags.optional("--price");
            if (priceStr != null) film.setPrice(new BigDecimal(priceStr));
            String description = flags.optional("--description");
            if (description != null) film.setDescription(description);
            String posterUrl = flags.optional("--poster-url");
            if (posterUrl != null) film.setPosterUrl(posterUrl);

            ctx.getFilmService().update(film);
            ctx.getOut().println("Фильм обновлён.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат числа.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}