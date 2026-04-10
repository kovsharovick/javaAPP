import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.repository.OrderRepository;
import org.example.repository.TicketRepository;
import org.example.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private TicketRepository ticketRepository;
    @InjectMocks private OrderServiceImpl orderService;

    @Test
    void findByStatus_delegates() {
        orderService.findByStatus(OrderStatus.COMPLETED);
        verify(orderRepository).findByStatus(OrderStatus.COMPLETED);
    }

    @Test
    void cancelOrder_success() throws Exception {
        Order order = new Order(1, 100, BigDecimal.valueOf(500), LocalDateTime.now(), OrderStatus.WAIT_PAYMENT, LocalDateTime.now().plusMinutes(10));
        when(orderRepository.findById(1)).thenReturn(order);
        doNothing().when(orderRepository).cancelOrderWithConnection(any(), eq(1));

        orderService.cancelOrder(1, 100, false);
        verify(orderRepository).cancelOrderWithConnection(any(), eq(1));
    }

    @Test
    void cancelOrder_wrongUser_throws() {
        Order order = new Order(1, 200, BigDecimal.ZERO, LocalDateTime.now(), OrderStatus.WAIT_PAYMENT, null);
        when(orderRepository.findById(1)).thenReturn(order);
        assertThrows(SecurityException.class, () -> orderService.cancelOrder(1, 100, false));
    }

    @Test
    void confirmPayment_success() throws Exception {
        Order order = new Order(1, 100, BigDecimal.valueOf(500), LocalDateTime.now(), OrderStatus.WAIT_PAYMENT, LocalDateTime.now().plusMinutes(5));
        when(orderRepository.findById(1)).thenReturn(order);
        doNothing().when(orderRepository).confirmPaymentWithConnection(any(), eq(1));

        orderService.confirmPayment(1, 100, false);
        verify(orderRepository).confirmPaymentWithConnection(any(), eq(1));
    }
}