import org.example.cli.CommandContext;
import org.example.cli.command.BuyCommand;
import org.example.model.Order;
import org.example.model.User;
import org.example.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyCommandTest {

    @Mock private CommandContext ctx;
    @Mock private TicketService ticketService;
    @Mock private PrintStream out;

    @Test
    void execute_validTickets_success() {
        User user = new User();
        user.setId(1);
        when(ctx.getCurrentUser()).thenReturn(user);
        when(ctx.getTicketService()).thenReturn(ticketService);
        when(ctx.getOut()).thenReturn(out);
        when(ticketService.getReservationMinutes()).thenReturn(15);

        Order order = new Order();
        order.setId(100);
        order.setAmount(BigDecimal.valueOf(500));
        when(ticketService.buyTickets(anyList())).thenReturn(order);

        String[] args = {"buy", "--ticket", "5:12", "--ticket", "5:13"};
        new BuyCommand().execute(ctx, args);

        verify(ticketService).buyTickets(argThat(list -> {
            List<TicketService.TicketDto> dtos = (List<TicketService.TicketDto>) list;
            return dtos.size() == 2 &&
                    dtos.get(0).sessionId() == 5 && dtos.get(0).placeId() == 12 &&
                    dtos.get(1).sessionId() == 5 && dtos.get(1).placeId() == 13;
        }));
        verify(out).printf(anyString(), eq(100), eq(BigDecimal.valueOf(500)), eq(15), eq(100));
    }
}