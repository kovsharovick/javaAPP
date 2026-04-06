package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class LoginCommand implements Command {
    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "login <email> <password> – вход в систему";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (ctx.getCurrentUser() != null) {
            ctx.getOut().println("Вы уже авторизованы. Сначала выполните logout.");
            return;
        }
        if (args.length < 3) {
            ctx.getOut().println("Использование: login <email> <password>");
            return;
        }
        String email = args[1];
        String password = args[2];
        boolean success = ctx.getAuthService().login(email, password);
        if (success) {
            User user = ctx.getAuthService().getCurrentUser();
            ctx.getOut().println("Добро пожаловать, " + user.getName() + "!");
        } else {
            ctx.getOut().println("Неверный email или пароль.");
        }
    }
}