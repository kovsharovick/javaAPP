package org.example.cli.admin_command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.List;
import java.util.Objects;

public class AdminOrderListCommand implements Command {
    @Override
    public String getName() {
        return "order list";
    }

    @Override
    public String getDescription() {
        return "order list [--status <status>] – список заказов (админ)";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        if (args.length < 2) {
            ctx.getOut().println("Использование: order list [--status <status>]");
            return;
        }
        try {
            List<Order> orders;
            if (args.length >= 4 && Objects.equals(args[2], "--status")) {
                String status = args[3];
                orders = ctx.getOrderService().findByStatus(OrderStatus.valueOf(status));
            } else {
                orders = ctx.getOrderService().getAll();
            }
            if (orders.isEmpty()) {
                ctx.getOut().println("Заказов нет.");
                return;
            }
            ctx.getOut().println("Список заказов: ");
            for (Order o : orders) {
                ctx.getOut().printf("id:  %d, статус: %s, пользователь: %d, сумма: %s, дата заказа: %s, время до отмены: %s\n",
                        o.getId(), o.getOrderStatus(), o.getUserId(), o.getAmount(), o.getDateTime(), o.getReservedUntil());
            }
        } catch (NumberFormatException e) {
            ctx.getOut().println("Неверный id заказа.");
        }
    }
}
