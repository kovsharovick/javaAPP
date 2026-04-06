package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;

public class AdminFilmDeleteCommand implements Command {
    @Override
    public String getName() {
        return "film delete";
    }

    @Override
    public String getDescription() {
        return "film delete <id> – удалить фильм (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: film delete <id>");
            return;
        }
        try {
            int id = Integer.parseInt(args[2]);
            boolean deleted = ctx.getFilmService().delete(id);
            if (deleted) {
                ctx.getOut().println("Фильм удалён.");
            } else {
                ctx.getOut().println("Фильм с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalStateException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}