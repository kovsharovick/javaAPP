import org.example.model.*;
import org.example.repository.*;
import org.example.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock private FilmRepository filmRepository;
    @Mock private HallRepository hallRepository;
    @InjectMocks private TicketServiceImpl ticketService;

    @Test
    void calculatePrice_standardPlace() {
        Session session = new Session();
        session.setFilmId(1);
        session.setHallId(1);
        Place place = new Place();
        place.setTypePlace(TypePlace.STANDARD);

        Film film = new Film();
        film.setPrice(BigDecimal.valueOf(100));
        Hall hall = new Hall();
        hall.setPrice(BigDecimal.valueOf(50));

        when(filmRepository.findById(1)).thenReturn(film);
        when(hallRepository.findById(1)).thenReturn(hall);

        BigDecimal price = ticketService.calculatePrice(session, place);
        assertEquals(new BigDecimal("125.0"), price);
    }

    @Test
    void calculatePrice_vipPlace() {
        Session session = new Session();
        session.setFilmId(1);
        session.setHallId(1);
        Place place = new Place();
        place.setTypePlace(TypePlace.VIP);

        Film film = new Film();
        film.setPrice(BigDecimal.valueOf(100));
        Hall hall = new Hall();
        hall.setPrice(BigDecimal.valueOf(50));

        when(filmRepository.findById(1)).thenReturn(film);
        when(hallRepository.findById(1)).thenReturn(hall);

        BigDecimal price = ticketService.calculatePrice(session, place);
        assertEquals(new BigDecimal("187.50"), price);
    }
}