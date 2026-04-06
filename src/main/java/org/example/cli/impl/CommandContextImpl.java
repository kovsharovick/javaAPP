package org.example.cli.impl;

import lombok.Getter;
import org.example.cli.CommandContext;
import org.example.model.User;
import org.example.service.*;

import java.io.PrintStream;
import java.util.Scanner;

public class CommandContextImpl implements CommandContext {
    private final AuthContext authContext;
    @Getter
    private final AuthService authService;
    @Getter
    private final UserService userService;
    @Getter
    private final FilmService filmService;
    @Getter
    private final HallService hallService;
    @Getter
    private final PlaceService placeService;
    @Getter
    private final SessionService sessionService;
    @Getter
    private final OrderService orderService;
    @Getter
    private final TicketService ticketService;
    @Getter
    private final Scanner scanner;
    @Getter
    private final PrintStream out;

    public CommandContextImpl(AuthContext authContext, AuthService authService, UserService userService, FilmService filmService, HallService hallService, PlaceService placeService, SessionService sessionService, OrderService orderService, TicketService ticketService, Scanner scanner, PrintStream out) {
        this.authContext = authContext;
        this.authService = authService;
        this.userService = userService;
        this.filmService = filmService;
        this.hallService = hallService;
        this.placeService = placeService;
        this.sessionService = sessionService;
        this.orderService = orderService;
        this.ticketService = ticketService;
        this.scanner = scanner;
        this.out = out;
    }

    @Override
    public User getCurrentUser() {
        return authContext.getCurrentUser();
    }
}