package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Hall;

public class AdminHallListCommand implements Command {
    @Override
    public String getName() {
        return "hall list";
    }

    @Override
    public String getDescription() {
        return "hall list – список залов";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        var halls = ctx.getHallService().getAll();
        if (halls.isEmpty()) {
            ctx.getOut().println("Залы не найдены.");
            return;
        }
        ctx.getOut().println("Список залов:");
        for (Hall h : halls) {
            ctx.getOut().printf("  %d. Рядов: %d, мест в ряду: %d, цена: %s%n",
                    h.getId(), h.getRows(), h.getSeatsPerRow(), h.getPrice());
        }
    }
}