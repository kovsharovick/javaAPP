package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class AdminRevokeCommand implements Command {
    @Override
    public String getName() {
        return "admin revoke";
    }

    @Override
    public String getDescription() {
        return "admin revoke <userId> –  снять права администратора (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: admin revoke <userId>");
            return;
        }
        try {
            int userId = Integer.parseInt(args[2]);
            User user = ctx.getUserService().getById(userId);
            if (user == null) {
                ctx.getOut().println("Пользователь с id " + userId + " не найден.");
                return;
            }
            if (!user.getAdmin()) {
                ctx.getOut().println("Пользователь не является администратором.");
                return;
            }
            ctx.getUserService().updateStatus(user, false);
            ctx.getOut().println("Пользователь " + user.getName() + " теперь не администратор.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        }
    }
}
