package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Film;

import java.math.BigDecimal;
import java.time.Duration;

public class AdminFilmCreateCommand implements Command {
    @Override
    public String getName() {
        return "film create";
    }

    @Override
    public String getDescription() {
        return "film create --name <name> --duration <min> --price <price> --description <text> --poster-url <url> – создать фильм (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 12) {
            ctx.getOut().println("Использование: film create --name <name> --duration <min> --price <price> --description <text> --poster-url <url>");
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String name = flags.require("--name");
        String durationStr = flags.require("--duration");
        String priceStr = flags.require("--price");
        String description = flags.require("--description");
        String posterUrl = flags.require("--poster-url");
        if (name == null || durationStr == null || priceStr == null || description == null || posterUrl == null) {
            ctx.getOut().println("Не указаны все обязательные параметры: --name, --duration, --price, --description, --poster-url");
            return;
        }
        try {
            int minutes = Integer.parseInt(durationStr);
            Duration duration = Duration.ofMinutes(minutes);
            BigDecimal price = new BigDecimal(priceStr);

            Film film = new Film();
            film.setName(name);
            film.setDuration(duration);
            film.setPrice(price);
            film.setDescription(description);
            film.setPosterUrl(posterUrl);
            film = ctx.getFilmService().save(film);
            ctx.getOut().println("Фильм создан, id=" + film.getId());
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат числа.");
        }
    }
}