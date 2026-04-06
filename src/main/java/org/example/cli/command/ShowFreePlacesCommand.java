package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Place;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowFreePlacesCommand implements Command {
    @Override
    public String getName() {
        return "session places";
    }

    @Override
    public String getDescription() {
        return "session places <sessionId> – показать свободные места";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 3) {
            ctx.getOut().println("Использование: session places <sessionId>");
            return;
        }
        int sessionId = Integer.parseInt(args[2]);
        List<Place> freePlaces = ctx.getPlaceService().getFreePlacesForSession(sessionId);
        if (freePlaces.isEmpty()) {
            ctx.getOut().println("Свободных мест нет.");
            return;
        }

        Map<Integer, List<Place>> byRow = freePlaces.stream().collect(Collectors.groupingBy(Place::getRows));
        for (Map.Entry<Integer, List<Place>> entry : byRow.entrySet()) {
            ctx.getOut().print("Ряд " + entry.getKey() + ": ");
            String seats = entry.getValue().stream()
                    .map(p -> String.valueOf(p.getSeat())).collect(Collectors.joining(", "));
            ctx.getOut().println(seats);
        }
    }
}