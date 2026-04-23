import org.example.service.impl.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        passwordHasher = new PasswordHasher();
    }

    @Test
    void hash_shouldReturnNotNull() {
        String hash = passwordHasher.hash("testPass");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void hash_shouldProduceDifferentHashesForSamePassword() {
        String pass = "same";
        String hash1 = passwordHasher.hash(pass);
        String hash2 = passwordHasher.hash(pass);
        assertNotEquals(hash1, hash2);
        assertTrue(passwordHasher.matches(pass, hash1));
        assertTrue(passwordHasher.matches(pass, hash2));
    }

    @Test
    void matches_shouldReturnTrueForCorrectPassword() {
        String pass = "correct";
        String hash = passwordHasher.hash(pass);
        assertTrue(passwordHasher.matches(pass, hash));
    }

    @Test
    void matches_shouldReturnFalseForIncorrectPassword() {
        String pass = "correct";
        String hash = passwordHasher.hash(pass);
        assertFalse(passwordHasher.matches("wrong", hash));
    }

    @Test
    void matches_shouldReturnFalseForNullPassword() {
        String hash = passwordHasher.hash("any");
        assertFalse(passwordHasher.matches(null, hash));
    }

    @Test
    void matches_shouldReturnFalseForNullHash() {
        assertFalse(passwordHasher.matches("any", null));
    }

    @Test
    void matches_shouldReturnFalseForInvalidHash() {
        assertFalse(passwordHasher.matches("any", "invalid$hash"));
    }

    @Test
    void hash_shouldThrowExceptionForNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> passwordHasher.hash(null));
    }

    @Test
    void hashFormat_shouldStartWithBcryptPrefix() {
        String hash = passwordHasher.hash("test");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
    }
}