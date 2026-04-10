import org.example.cli.CommandContext;
import org.example.cli.command.RegisterCommand;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCommandTest {

    @Mock private CommandContext ctx;
    @Mock private UserService userService;
    @Mock private PrintStream out;

    @Test
    void execute_validInput_registersUser() {
        when(ctx.getUserService()).thenReturn(userService);
        when(ctx.getOut()).thenReturn(out);
        User user = new User();
        user.setName("Alice");
        when(userService.register("Alice", "alice@mail.com", "pass", false)).thenReturn(user);

        String[] args = {"register", "Alice", "alice@mail.com", "pass"};
        new RegisterCommand().execute(ctx, args);
        verify(out).println("Пользователь Alice зарегистрирован.");
    }

    @Test
    void execute_missingArguments_printsUsage() {
        when(ctx.getOut()).thenReturn(out);
        String[] args = {"register", "Alice"};
        new RegisterCommand().execute(ctx, args);
        verify(out).println("Использование: register <name> <email> <password>");
        verify(userService, never()).register(any(), any(), any(), anyBoolean());
    }

    @Test
    void execute_duplicateEmail_printsError() {
        when(ctx.getUserService()).thenReturn(userService);
        when(ctx.getOut()).thenReturn(out);
        when(userService.register(any(), any(), any(), anyBoolean()))
                .thenThrow(new IllegalArgumentException("Email уже существует"));

        String[] args = {"register", "Bob", "bob@mail.com", "pass"};
        new RegisterCommand().execute(ctx, args);
        verify(out).println("Ошибка: Email уже существует");
    }
}