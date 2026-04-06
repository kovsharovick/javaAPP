package org.example.cli;

import org.example.model.User;
import org.example.service.*;

import java.io.PrintStream;
import java.util.Scanner;

public interface CommandContext {
    User getCurrentUser();
    AuthService getAuthService();
    UserService getUserService();
    FilmService getFilmService();
    HallService getHallService();
    PlaceService getPlaceService();
    SessionService getSessionService();
    OrderService getOrderService();
    TicketService getTicketService();
    Scanner getScanner();
    PrintStream getOut();
}