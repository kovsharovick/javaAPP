

import org.example.model.Film;
import org.example.model.Session;
import org.example.repository.FilmRepository;
import org.example.repository.SessionRepository;
import org.example.repository.TicketRepository;
import org.example.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private FilmRepository filmRepository;
    @Mock private TicketRepository ticketRepository;
    @InjectMocks private SessionServiceImpl sessionService;

    @Test
    void createSession_noOverlap_success() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(120));
        when(filmRepository.findById(1)).thenReturn(film);
        when(sessionRepository.existsOverlap(10, LocalDateTime.of(2025,1,1,10,0),
                LocalDateTime.of(2025,1,1,12,0), null)).thenReturn(false);
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session); // было doNothing

        assertDoesNotThrow(() -> sessionService.createSession(10, 1, LocalDateTime.of(2025,1,1,10,0)));
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void createSession_overlap_throws() {
        Film film = new Film();
        film.setDuration(Duration.ofMinutes(120));
        when(filmRepository.findById(1)).thenReturn(film);
        when(sessionRepository.existsOverlap(10, LocalDateTime.of(2025,1,1,10,0),
                LocalDateTime.of(2025,1,1,12,0), null)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> sessionService.createSession(10, 1, LocalDateTime.of(2025,1,1,10,0)));
        verify(sessionRepository, never()).save(any());
    }
}