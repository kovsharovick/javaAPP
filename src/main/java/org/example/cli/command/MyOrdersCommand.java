package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Order;
import org.example.model.User;

import java.util.List;

public class MyOrdersCommand implements Command {
    @Override
    public String getName() {
        return "orders";
    }

    @Override
    public String getDescription() {
        return "orders – показать мои заказы";
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
        List<Order> orders = ctx.getOrderService().findByUserId(user.getId());
        if (orders.isEmpty()) {
            ctx.getOut().println("У вас нет заказов.");
            return;
        }
        for (Order order : orders) {
            ctx.getOut().printf("Заказ #%d, сумма: %s, статус: %s, дата: %s\n",
                    order.getId(), order.getAmount(), order.getOrderStatus(), order.getDateTime());
        }
    }
}