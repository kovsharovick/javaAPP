package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

public class PayCommand implements Command {
    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getDescription() {
        return "pay <orderId> – оплатить заказ";
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
            ctx.getOut().println("Использование: pay <orderId>");
            return;
        }
        int orderId = Integer.parseInt(args[1]);
        try {
            ctx.getOrderService().confirmPayment(orderId, user.getId(), user.getAdmin());
            ctx.getOut().println("Заказ оплачен. Билеты отправлены на почту.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ctx.getOut().println("Ошибка: " + e.getMessage());
        }
    }
}