package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;

public class AdminSessionDeleteCommand implements Command {
    @Override
    public String getName() {
        return "session delete";
    }

    @Override
    public String getDescription() {
        return "session delete <id> [--force] – удалить сеанс (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: session delete <id> [--force]");
            return;
        }
        try {
            int id = Integer.parseInt(args[2]);
            boolean force = args.length > 3 && "--force".equals(args[3]);
            boolean deleted = ctx.getSessionService().delete(id, force);
            if (deleted) {
                ctx.getOut().println("Сеанс удалён.");
            } else {
                ctx.getOut().println("Сеанс с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalStateException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}