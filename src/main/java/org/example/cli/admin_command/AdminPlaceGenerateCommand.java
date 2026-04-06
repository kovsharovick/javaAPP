package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;

public class AdminPlaceGenerateCommand implements Command {
    @Override
    public String getName() {
        return "place generate";
    }

    @Override
    public String getDescription() {
        return "place generate <hallId> – сгенерировать места для зала (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: place generate <hallId>");
            return;
        }
        try {
            int hallId = Integer.parseInt(args[2]);
            ctx.getPlaceService().generatePlacesForHall(hallId);
            ctx.getOut().println("Места для зала " + hallId + " сгенерированы.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}