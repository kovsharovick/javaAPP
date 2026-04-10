import org.example.model.Film;
import org.example.model.Session;
import org.example.repository.FilmRepository;
import org.example.repository.SessionRepository;
import org.example.service.impl.FilmServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {

    @Mock private FilmRepository filmRepository;
    @Mock private SessionRepository sessionRepository;
    @InjectMocks private FilmServiceImpl filmService;

    @Test
    void delete_filmWithNoSessions_success() {
        when(sessionRepository.findByFilmId(1)).thenReturn(List.of());
        when(filmRepository.delete(1)).thenReturn(true);
        assertTrue(filmService.delete(1));
        verify(filmRepository).delete(1);
    }

    @Test
    void delete_filmWithSessions_throws() {
        when(sessionRepository.findByFilmId(1)).thenReturn(List.of(new Session()));
        assertThrows(IllegalStateException.class, () -> filmService.delete(1));
        verify(filmRepository, never()).delete(any());
    }

    @Test
    void findByNameContaining_delegates() {
        filmService.findByNameContaining("test");
        verify(filmRepository).findByNameContaining("test");
    }
}