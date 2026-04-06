package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Hall;

import java.math.BigDecimal;

public class AdminHallUpdateCommand implements Command {
    @Override
    public String getName() {
        return "hall update";
    }

    @Override
    public String getDescription() {
        return "hall update --id <id> --price <price> – обновление зала (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 6) {
            ctx.getOut().println("Использование: hall update --id <id> --price <price>");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String idStr = flags.require("--id");
        String priceStr = flags.require("--price");
        if (idStr == null || priceStr == null) return;
        try {

            int hallId = Integer.parseInt(idStr);
            Hall hall = ctx.getHallService().getById(hallId);
            if (hall == null) {
                ctx.getOut().println("Зал не найден.");
                return;
            }
            BigDecimal price = new BigDecimal(priceStr);
            hall.setPrice(price);
            ctx.getHallService().update(hall);
            ctx.getOut().println("Цена зала обновлёна.");
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Неверный тип.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}
