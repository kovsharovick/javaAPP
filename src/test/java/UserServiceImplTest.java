import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.PasswordHasher;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordHasher passwordHasher;
    @InjectMocks private UserServiceImpl userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User(1, "hashedPass", "John", "john@mail.com", false);
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(passwordHasher.hash("pass123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(2);
            return u;
        });

        User user = userService.register("NewUser", "new@mail.com", "pass123", false);
        assertNotNull(user.getId());
        assertEquals("NewUser", user.getName());
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("hashed", user.getPassword());
        assertFalse(user.getAdmin());
        verify(userRepository).save(any());
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByEmail("dup@mail.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("Dupe", "dup@mail.com", "pwd", false));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_invalidEmail_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("Bad", "not-an-email", "pwd", false));
    }

    @Test
    void updateProfile_changeNameAndEmail_success() {
        when(userRepository.existsByEmail("newemail@mail.com")).thenReturn(false);
        doNothing().when(userRepository).update(any(User.class));

        userService.updateProfile(existingUser, "Johnny", "newemail@mail.com", null);

        verify(userRepository).update(argThat(user ->
                user.getId() == 1 &&
                user.getName().equals("Johnny") &&
                user.getEmail().equals("newemail@mail.com") &&
                user.getPassword().equals("hashedPass")
        ));
        assertEquals("Johnny", existingUser.getName());
        assertEquals("newemail@mail.com", existingUser.getEmail());
    }

    @Test
    void updateProfile_emailAlreadyExists_throws() {
        when(userRepository.existsByEmail("taken@mail.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateProfile(existingUser, null, "taken@mail.com", null));
    }

    @Test
    void updateProfile_invalidEmail_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateProfile(existingUser, null, "bad-email", null));
    }

    @Test
    void updateStatus_grantAdmin_success() {
        userService.updateStatus(existingUser, true);
        assertTrue(existingUser.getAdmin());
        verify(userRepository).update(existingUser);
    }

    @Test
    void updateStatus_nullUser_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateStatus(null, true));
    }
}