package org.example.cli;

import org.example.cli.impl.CommandContextImpl;
import org.example.config.DatabaseConnection;
import org.example.repository.*;
import org.example.repository.impl.*;
import org.example.service.*;
import org.example.service.impl.*;
import org.example.service.impl.AuthContextImpl;
import org.example.service.impl.AuthServiceImpl;
import org.example.service.impl.PasswordHasher;
import org.example.cli.command.*;
import org.example.cli.admin_command.*;

import java.util.*;
import java.util.Scanner;

public class CliApplication {
    public void run() {
        try {
            DatabaseConnection.getConnection().close();
        } catch (Exception e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            return;
        }

        UserRepository userRepository = new UserRepositoryImpl();
        FilmRepository filmRepository = new FilmRepositoryImpl();
        HallRepository hallRepository = new HallRepositoryImpl();
        PlaceRepository placeRepository = new PlaceRepositoryImpl();
        SessionRepository sessionRepository = new SessionRepositoryImpl();
        OrderRepository orderRepository = new OrderRepositoryImpl();
        TicketRepository ticketRepository = new TicketRepositoryImpl();

        PasswordHasher passwordHasher = new PasswordHasher(); // или заглушка
        AuthContext authContext = new AuthContextImpl();
        AuthService authService = new AuthServiceImpl(userRepository, authContext, passwordHasher);

        UserService userService = new UserServiceImpl(userRepository, passwordHasher);
        FilmService filmService = new FilmServiceImpl(sessionRepository, filmRepository);
        HallService hallService = new HallServiceImpl(hallRepository, sessionRepository, placeRepository);
        PlaceService placeService = new PlaceServiceImpl(placeRepository, hallRepository, ticketRepository, sessionRepository);
        SessionService sessionService = new SessionServiceImpl(sessionRepository, filmRepository, ticketRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, ticketRepository);
        TicketService ticketService = new TicketServiceImpl(ticketRepository, orderRepository, sessionRepository,
                placeRepository, filmRepository, hallRepository, userRepository, authContext);

        CommandRegistry registry = new CommandRegistry();
        registry.register(new LoginCommand());
        registry.register(new LogoutCommand());
        registry.register(new RegisterCommand());
        registry.register(new ProfileCommand());
        registry.register(new FilmListCommand());
        registry.register(new SessionListCommand());
        registry.register(new ShowFreePlacesCommand());
        registry.register(new BuyCommand());
        registry.register(new PayCommand());
        registry.register(new CancelOrderCommand());
        registry.register(new MyOrdersCommand());
        registry.register(new SessionInfoCommand());
        registry.register(new FilmInfoCommand());


        registry.register(new AdminFilmCreateCommand());
        registry.register(new AdminFilmUpdateCommand());
        registry.register(new AdminFilmDeleteCommand());
        registry.register(new AdminHallCreateCommand());
        registry.register(new AdminHallListCommand());
        registry.register(new AdminHallDeleteCommand());
        registry.register(new AdminHallUpdateCommand());
        registry.register(new AdminOrderListCommand());
        registry.register(new AdminSessionCreateCommand());
        registry.register(new AdminSessionDeleteCommand());
        registry.register(new AdminSessionUpdateCommand());
        registry.register(new AdminPlaceGenerateCommand());
        registry.register(new AdminPlaceListCommand());
        registry.register(new AdminPlaceUpdateCommand());
        registry.register(new AdminTicketListCommand());
        registry.register(new AdminGrantCommand());
        registry.register(new AdminRevokeCommand());
        registry.register(new AdminRevenueCommand());

        registry.register(new HelpCommand(registry.getAllCommands()));

        CommandContext ctx = new CommandContextImpl(authContext, authService, userService,
                filmService, hallService, placeService, sessionService, orderService, ticketService,
                new Scanner(System.in), System.out);

        ctx.getOut().println("Кинотеатр. Введите help для списка команд, exit – выход.");

        while (true) {
            if (ctx.getCurrentUser() != null) {
                ctx.getOut().print(ctx.getCurrentUser().getName() + (ctx.getCurrentUser().getAdmin() ? " (admin)" : "") + "> ");
            } else {
                ctx.getOut().print("> ");
            }
            if (!ctx.getScanner().hasNextLine()) break;
            String line = ctx.getScanner().nextLine().trim();
            if (line.isEmpty()) continue;
            if ("exit".equalsIgnoreCase(line) || "quit".equalsIgnoreCase(line)) {
                ctx.getOut().println("Пока, пока!");
                break;
            }
            String[] parts = line.split("\\s+");
            CommandLookup lookup = registry.lookup(parts);
            Optional<Command> cmdOpt = registry.find(lookup.key());
            if (cmdOpt.isEmpty()) {
                ctx.getOut().println("Неизвестная команда: " + lookup.displayName());
                continue;
            }
            Command cmd = cmdOpt.get();
            if (!registry.canExecute(cmd, ctx.getCurrentUser())) {
                ctx.getOut().println("Нет прав для выполнения команды: " + lookup.displayName());
                continue;
            }
            try {
                cmd.execute(ctx, parts);
            } catch (Exception e) {
                ctx.getOut().println("Ошибка: " + e.getMessage());
                e.printStackTrace(ctx.getOut());
            }
        }
    }
}