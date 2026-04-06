package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class AdminGrantCommand implements Command {
    @Override
    public String getName() {
        return "admin grant";
    }

    @Override
    public String getDescription() {
        return "admin grant <userId> – назначить пользователя администратором (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: admin grant <userId>");
            return;
        }
        try {
            int userId = Integer.parseInt(args[2]);
            User user = ctx.getUserService().getById(userId);
            if (user == null) {
                ctx.getOut().println("Пользователь с id " + userId + " не найден.");
                return;
            }
            if (user.getAdmin()) {
                ctx.getOut().println("Пользователь уже является администратором.");
                return;
            }
            ctx.getUserService().updateStatus(user, true);
            ctx.getOut().println("Пользователь " + user.getName() + " теперь администратор.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        }
    }
}
