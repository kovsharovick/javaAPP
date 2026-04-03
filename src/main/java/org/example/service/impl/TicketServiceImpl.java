package org.example.service.impl;

import org.example.config.DatabaseConnection;
import org.example.model.*;
import org.example.repository.*;
import org.example.service.AuthContext;
import org.example.service.TicketService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    private static final int RESERVATION_MINUTES = 15;
    private static final BigDecimal HALL_PRICE_FACTOR = BigDecimal.valueOf(0.5);
    private static final BigDecimal VIP_PRICE_FACTOR = BigDecimal.valueOf(1.5);
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final SessionRepository sessionRepository;
    private final PlaceRepository placeRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;
    private final AuthContext authContext;

    public TicketServiceImpl(TicketRepository ticketRepository, OrderRepository orderRepository, SessionRepository sessionRepository, PlaceRepository placeRepository, FilmRepository filmRepository, HallRepository hallRepository, UserRepository userRepository, AuthContext authContext) {
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        this.sessionRepository = sessionRepository;
        this.placeRepository = placeRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
        this.authContext = authContext;
    }

    @Override
    public Order buyTickets(List<TicketDto> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("Не выбрано ни одного билета");
        }
        User currentUser = authContext.getCurrentUser();
        if (currentUser == null) throw new IllegalStateException("Необходимо войти в систему");
        Integer userId = currentUser.getId();

        List<TicketData> ticketDataList = new ArrayList<>();
        for (TicketDto dto : tickets) {
            Session session = sessionRepository.findById(dto.sessionId());
            if (session == null) {
                throw new IllegalArgumentException("Сеанс " + dto.sessionId() + " не найден");
            }
            Place place = placeRepository.findById(dto.placeId());
            if (place == null) {
                throw new IllegalArgumentException("Место " + dto.placeId() + " не найдено");
            }
            if (!place.getHallId().equals(session.getHallId())) {
                throw new IllegalArgumentException("Место " + dto.placeId() + " не принадлежит залу сеанса " + dto.sessionId());
            }
            if (session.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Сеанс " + dto.sessionId() + " уже начался");
            }
            if (place.getRows() == 0 || place.getSeat() == 0) {
                throw new IllegalArgumentException("Место " + dto.placeId() + " удалено и недоступно для покупки");
            }
            ticketDataList.add(new TicketData(session, place));
        }


        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            for (TicketData td : ticketDataList) {
                if (!ticketRepository.isPlaceFree(conn, td.session.getId(), td.place.getId())) {
                    throw new IllegalArgumentException("Место " + td.place.getId() + " уже занято");
                }
            }

            Order order = orderRepository.createOrderWithConnection(conn, userId, RESERVATION_MINUTES);

            BigDecimal total = BigDecimal.ZERO;
            for (TicketData ctx : ticketDataList) {
                BigDecimal price = calculatePrice(ctx.session, ctx.place);

                Ticket ticket = new Ticket();
                ticket.setOrdersId(order.getId());
                ticket.setPlaceId(ctx.place.getId());
                ticket.setSessionId(ctx.session.getId());
                ticket.setPrice(price);
                ticket.setTicketStatus(TicketStatus.RESERVED);

                ticketRepository.saveWithConnection(conn, ticket);
                total = total.add(price);
            }

            order.setAmount(total);
            orderRepository.updateWithConnection(conn, order);

            conn.commit();
            return order;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Ошибка при резервировании билетов", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Ticket> getTicketsByOrderId(Integer orderId) {
        return ticketRepository.findByOrderId(orderId);
    }

    @Override
    public Ticket getById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    private BigDecimal calculatePrice(Session session, Place place) {
        Film film = filmRepository.findById(session.getFilmId());
        Hall hall = hallRepository.findById(session.getHallId());
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        if (hall == null) {
            throw new RuntimeException("Сеанс не найден");
        }
        BigDecimal hallPrice = hall.getPrice() != null ? hall.getPrice() : BigDecimal.ZERO;
        BigDecimal basePrice = film.getPrice().add(hallPrice.multiply(HALL_PRICE_FACTOR));
        if (place.getTypePlace() == TypePlace.VIP) {
            return basePrice.multiply(VIP_PRICE_FACTOR);
        }
        return basePrice;
    }

    private static class TicketData {
        final Session session;
        final Place place;

        TicketData(Session session, Place place) {
            this.session = session;
            this.place = place;
        }
    }
}