package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;

public class AdminHallDeleteCommand implements Command {
    @Override
    public String getName() {
        return "hall delete";
    }

    @Override
    public String getDescription() {
        return "hall delete <id> – удалить зал (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: hall delete <id>");
            return;
        }
        try {
            int id = Integer.parseInt(args[2]);
            boolean deleted = ctx.getHallService().delete(id);
            if (deleted) {
                ctx.getOut().println("Зал удалён.");
            } else {
                ctx.getOut().println("Зал с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalStateException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}