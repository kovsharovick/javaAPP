import org.example.cli.CommandContext;
import org.example.cli.admin_command.AdminGrantCommand;
import org.example.cli.command.LoginCommand;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminGrantCommandTest {

    @Mock private CommandContext ctx;
    @Mock private UserService userService;
    @Mock private PrintStream out;

    @Test
    void execute_grantAdmin_success() {
        User user = new User();
        user.setName("Bob");
        user.setAdmin(false);
        when(ctx.getUserService()).thenReturn(userService);
        when(ctx.getOut()).thenReturn(out);
        when(userService.getById(5)).thenReturn(user);

        String[] args = {"admin", "grant", "5"};
        new AdminGrantCommand().execute(ctx, args);
        verify(userService).updateStatus(user, true);
        verify(out).println("Пользователь Bob теперь администратор.");
    }

    @Test
    void execute_userNotFound_printsError() {
        when(ctx.getUserService()).thenReturn(userService);
        when(ctx.getOut()).thenReturn(out);
        when(userService.getById(99)).thenReturn(null);
        String[] args = {"admin", "grant", "99"};
        new AdminGrantCommand().execute(ctx, args);
        verify(out).println("Пользователь с id 99 не найден.");
        verify(userService, never()).updateStatus(any(), anyBoolean());
    }
}
