package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Film;

public class FilmInfoCommand implements Command {
    @Override
    public String getName() {
        return "film info";
    }

    @Override
    public String getDescription() {
        return "film info <id> – показать информацию о фильме";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: film info <id>");
            return;
        }
        Film film = ctx.getFilmService().getById(Integer.valueOf(args[2]));
        if (film == null) {
            ctx.getOut().println("Фильм не найден.");
            return;
        }
        ctx.getOut().println("Информация о фильме:");
        ctx.getOut().printf("  %d. Фильм: %s, Длительность: %s, Цена (Итоговая цена зависит от зала и места): %s \nОписание: %s\n",
                film.getId(), film.getName(), film.getDuration(), film.getPrice(), film.getDescription());
    }
}