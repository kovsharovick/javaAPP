package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Place;
import org.example.model.TypePlace;

public class AdminPlaceUpdateCommand implements Command {
    @Override
    public String getName() {
        return "place update-type";
    }

    @Override
    public String getDescription() {
        return "place update-type --id <placeId> --type VIP|STANDARD. – обновление места (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 6) {
            ctx.getOut().println("Использование: place update-type --id <placeId> --type VIP|STANDARD");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String idStr = flags.require("--id");
        String typeStr = flags.require("--type");
        if (idStr == null || typeStr == null) return;
        try {
            int placeId = Integer.parseInt(idStr);
            Place place = ctx.getPlaceService().getById(placeId);
            if (place == null) {
                ctx.getOut().println("Место не найдено.");
                return;
            }
            TypePlace type = TypePlace.valueOf(typeStr.toUpperCase());
            place.setTypePlace(type);
            ctx.getPlaceService().update(place);
            ctx.getOut().println("Тип места обновлён.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Неверный тип. Используйте VIP или STANDARD.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}
