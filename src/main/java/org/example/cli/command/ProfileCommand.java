package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.User;

public class ProfileCommand implements Command {
    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "profile [--name <name>] [--email <email>] [--password <pwd>] – просмотр/изменение профиля";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        User current = ctx.getCurrentUser();
        if (current == null) {
            ctx.getOut().println("Необходимо войти в систему.");
            return;
        }
        if (args.length == 1) {
            ctx.getOut().printf("Имя: %s\nEmail: %s\nАдминистратор: %s\n",
                    current.getName(), current.getEmail(), current.getAdmin() ? "да" : "нет");
            return;
        }

        FlagArgs flags = FlagArgs.parse(args, 1);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String newName = flags.optional("--name");
        String newEmail = flags.optional("--email");
        String newPassword = flags.optional("--password");
        try {
            ctx.getUserService().updateProfile(current, newName, newEmail, newPassword);
            ctx.getOut().println("Профиль обновлён.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}