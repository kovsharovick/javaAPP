package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;

public class LogoutCommand implements Command {
    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public String getDescription() {
        return "logout – выход из системы";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (ctx.getCurrentUser() == null) {
            ctx.getOut().println("Вы не авторизованы.");
            return;
        }
        ctx.getAuthService().logout();
        ctx.getOut().println("Вы вышли из системы.");
    }
}