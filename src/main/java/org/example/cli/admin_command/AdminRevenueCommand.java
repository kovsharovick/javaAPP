package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.cli.FlagArgs;
import org.example.model.Hall;
import org.example.model.Order;
import org.example.model.Place;
import org.example.model.TypePlace;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminRevenueCommand implements Command {
    @Override
    public String getName() {
        return "report revenue";
    }

    @Override
    public String getDescription() {
        return "report revenue [--from YYYY-MM-DD] [--to YYYY-MM-DD] – отчет о выручке (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 2) {
            ctx.getOut().println("Использование: report revenue [--from YYYY-MM-DD] [--to YYYY-MM-DD]");
            return;
        }
        FlagArgs flags = FlagArgs.parse(args, 2);
        if (flags.getError() != null) {
            ctx.getOut().println(flags.getError());
            return;
        }
        String fromStr = flags.require("--from");
        String toStr = flags.optional("--to");
        try {
            LocalDate fromDate = fromStr != null ? LocalDate.parse(fromStr) : null;
            LocalDate toDate = toStr != null ? LocalDate.parse(toStr) : LocalDate.now();
            LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;
            LocalDateTime to = toDate.plusDays(1).atStartOfDay();
            BigDecimal revenue = ctx.getOrderService().sumRevenueByPeriod(from, to);
            ctx.getOut().println("Доход за выбранный период: " + revenue);
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id.");
        } catch (IllegalArgumentException e) {
            ctx.getOut().println("Неверный тип даты.");
        } catch (Exception e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}
