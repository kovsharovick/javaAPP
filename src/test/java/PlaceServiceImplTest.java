import org.example.model.*;
import org.example.repository.*;
import org.example.service.impl.PlaceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

    @Mock private PlaceRepository placeRepository;
    @Mock private HallRepository hallRepository;
    @Mock private TicketRepository ticketRepository;
    @Mock private SessionRepository sessionRepository;
    @InjectMocks private PlaceServiceImpl placeService;

    @Test
    void generatePlacesForHall_deletesOldAndGeneratesNew() {
        Hall hall = new Hall(1, 3, 5, BigDecimal.TEN);
        when(hallRepository.findById(1)).thenReturn(hall);
        Place place1 = new Place(); place1.setId(101);
        Place place2 = new Place(); place2.setId(102);
        when(placeRepository.findByHallId(1)).thenReturn(List.of(place1, place2));

        placeService.generatePlacesForHall(1);
        verify(placeRepository).delete(101);
        verify(placeRepository).delete(102);
        verify(placeRepository, times(15)).save(any(Place.class));
    }

    @Test
    void getFreePlacesForSession_filtersTaken() {
        Session session = new Session();
        session.setHallId(1);
        when(sessionRepository.findById(100)).thenReturn(session);

        Place p1 = new Place(); p1.setId(1);
        Place p2 = new Place(); p2.setId(2);
        Place p3 = new Place(); p3.setId(3);
        when(placeRepository.findByHallId(1)).thenReturn(List.of(p1, p2, p3));

        Ticket taken = new Ticket();
        taken.setPlaceId(2);
        taken.setTicketStatus(TicketStatus.SOLD);
        when(ticketRepository.findBySessionId(100)).thenReturn(List.of(taken));

        List<Place> free = placeService.getFreePlacesForSession(100);
        assertEquals(2, free.size());
        assertTrue(free.stream().noneMatch(p -> p.getId() == 2));
    }
}