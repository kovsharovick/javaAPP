package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class RegisterCommand implements Command {
    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register <name> <email> <password> – регистрация нового пользователя";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 4) {
            ctx.getOut().println("Использование: register <name> <email> <password>");
            return;
        }
        String name = args[1];
        String email = args[2];
        String password = args[3];
        try {
            User user = ctx.getUserService().register(name, email, password, false);
            ctx.getOut().println("Пользователь " + user.getName() + " зарегистрирован.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}