package org.example.web.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.repository.*;
import org.example.repository.impl.*;
import org.example.service.*;
import org.example.service.impl.*;
import org.example.service.impl.AuthContextImpl;
import org.example.service.impl.PasswordHasher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserRepository userRepository = new UserRepositoryImpl();
        FilmRepository filmRepository = new FilmRepositoryImpl();
        HallRepository hallRepository = new HallRepositoryImpl();
        PlaceRepository placeRepository = new PlaceRepositoryImpl();
        SessionRepository sessionRepository = new SessionRepositoryImpl();
        OrderRepository orderRepository = new OrderRepositoryImpl();
        TicketRepository ticketRepository = new TicketRepositoryImpl();

        PasswordHasher passwordHasher = new PasswordHasher();
        AuthContext authContext = new AuthContextImpl();

        UserService userService = new UserServiceImpl(userRepository, passwordHasher, authContext);
        AuthService authService = new AuthServiceImpl(userRepository, authContext, passwordHasher);
        FilmService filmService = new FilmServiceImpl(sessionRepository, filmRepository, ticketRepository, authContext);
        HallService hallService = new HallServiceImpl(hallRepository, sessionRepository, placeRepository, authContext);
        PlaceService placeService = new PlaceServiceImpl(placeRepository, hallRepository, ticketRepository, sessionRepository, authContext);
        SessionService sessionService = new SessionServiceImpl(sessionRepository, filmRepository, ticketRepository, authContext);
        OrderService orderService = new OrderServiceImpl(orderRepository, ticketRepository, authContext);
        TicketService ticketService = new TicketServiceImpl(ticketRepository, orderRepository, sessionRepository,
                placeRepository, filmRepository, hallRepository, userRepository, authContext);

        var ctx = sce.getServletContext();
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("authService", authService);
        ctx.setAttribute("filmService", filmService);
        ctx.setAttribute("hallService", hallService);
        ctx.setAttribute("placeService", placeService);
        ctx.setAttribute("sessionService", sessionService);
        ctx.setAttribute("orderService", orderService);
        ctx.setAttribute("ticketService", ticketService);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                OrderService os = (OrderService) ctx.getAttribute("orderService");
                SessionService ss = (SessionService) ctx.getAttribute("sessionService");
                if (os != null && ss != null) {
                    os.cancelExpiredReservations();
                    ss.markFinishedSessions();
                    System.out.println("Фоновая задача выполнена: отмена просроченных заказов и пометка использованных билетов");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES);
        ctx.setAttribute("scheduler", scheduler);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ScheduledExecutorService scheduler = (ScheduledExecutorService) sce.getServletContext().getAttribute("scheduler");
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}