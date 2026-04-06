package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Hall;
import org.example.model.Place;

import java.util.List;

public class AdminPlaceListCommand implements Command {
    @Override
    public String getName() {
        return "place list";
    }

    @Override
    public String getDescription() {
        return "place list <hallId> – список мест зала (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: place list <hallId>");
            return;
        }
        try {
            int hallId = Integer.parseInt(args[2]);
            Hall hall = ctx.getHallService().getById(hallId);
            if (hall == null) {
                ctx.getOut().println("Зал не найден.");
                return;
            }
            ctx.getOut().println("Список мест в зале " + hallId + ":");
            List<Place> places = ctx.getPlaceService().findByHallId(hallId);
            for (Place p : places) {
                ctx.getOut().printf("Ряд:  %d, место: %d, тип места: %s, id: %d\n",
                        p.getRows(), p.getSeat(), p.getTypePlace(), p.getId());
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id зала.");
        }
    }
}
