package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Hall;

import java.math.BigDecimal;

public class AdminHallCreateCommand implements Command {
    @Override
    public String getName() {
        return "hall create";
    }

    @Override
    public String getDescription() {
        return "hall create --rows <число> --seats-per-row <число> --price <цена> – создать зал (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String rowsStr = flags.require("--rows");
        String seatsStr = flags.require("--seats-per-row");
        String priceStr = flags.require("--price");
        if (rowsStr == null || seatsStr == null || priceStr == null) return;
        try {
            int rows = Integer.parseInt(rowsStr);
            int seatsPerRow = Integer.parseInt(seatsStr);
            BigDecimal price = new BigDecimal(priceStr);
            Hall hall = new Hall();
            hall.setRows(rows);
            hall.setSeatsPerRow(seatsPerRow);
            hall.setPrice(price);
            hall = ctx.getHallService().save(hall);
            ctx.getOut().println("Зал создан, id=" + hall.getId());
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный формат числа.");
        }
    }
}