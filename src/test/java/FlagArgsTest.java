

import org.example.cli.Command;
import org.example.cli.CommandRegistry;
import org.example.cli.FlagArgs;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FlagArgsTest {

    @Test
    void parse_singleFlag() {
        String[] args = {"cmd", "--session", "5", "--place", "12"};
        FlagArgs flags = FlagArgs.parse(args, 1);
        assertNull(flags.getError());
        assertEquals("5", flags.require("--session"));
        assertEquals(List.of("12"), flags.all("--place"));
    }

    @Test
    void parse_missingValue_returnsError() {
        String[] args = {"cmd", "--session"};
        FlagArgs flags = FlagArgs.parse(args, 1);
        assertNotNull(flags.getError());
        assertTrue(flags.getError().contains("Не указано значение"));
    }

    @Test
    void require_missingFlag_setsError() {
        FlagArgs flags = FlagArgs.parse(new String[]{"cmd"}, 1);
        assertNull(flags.require("--any"));
        assertNotNull(flags.getError());
    }
}