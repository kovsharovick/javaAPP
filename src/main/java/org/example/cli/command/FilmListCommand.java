package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Film;

import java.util.List;

public class FilmListCommand implements Command {
    @Override
    public String getName() {
        return "film list";
    }

    @Override
    public String getDescription() {
        return "film list [--search <text>] – показать фильмы";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        List<Film> films;
        if (args.length >= 4 && "--search".equals(args[2])) {
            String search = args[3];
            films = ctx.getFilmService().findByNameContaining(search);
        } else {
            films = ctx.getFilmService().getAll();
        }
        if (films.isEmpty()) {
            ctx.getOut().println("Фильмы не найдены.");
            return;
        }
        ctx.getOut().println("Список фильмов:");
        for (Film f : films) {
            ctx.getOut().printf("  %d. %s (%d мин) – %s руб.\n",
                    f.getId(), f.getName(), f.getDuration().toMinutes(), f.getPrice());
        }
    }
}