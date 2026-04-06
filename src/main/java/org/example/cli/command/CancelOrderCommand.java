package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class CancelOrderCommand implements Command {
    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "cancel <orderId> – отменить заказ (если не оплачен)";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        User user = ctx.getCurrentUser();
        if (user == null) {
            ctx.getOut().println("Необходимо войти в систему.");
            return;
        }
        if (args.length < 2) {
            ctx.getOut().println("Использование: cancel <orderId>");
            return;
        }
        int orderId = Integer.parseInt(args[1]);
        try {
            ctx.getOrderService().cancelOrder(orderId, user.getId(), user.getAdmin());
            ctx.getOut().println("Заказ отменён.");
        } catch (IllegalArgumentException | SecurityException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}