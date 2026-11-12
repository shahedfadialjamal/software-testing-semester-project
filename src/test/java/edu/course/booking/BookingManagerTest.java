
package edu.course.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingManagerTest {

    @Mock
    private IPaymentGateway paymentGateway;

    @Mock
    private INotificationService notificationService;

    @Mock
    private IEventRepository eventRepository;

    @Test
    void shouldBookSuccessfully() {

        BookingRequest request =
                new BookingRequest("C1", "E1", 2, 1500);

        when(eventRepository.isSoldOut("E1"))
                .thenReturn(false);

        when(paymentGateway.processPayment("C1", "E1", 3000))
                .thenReturn("TX123");

        BookingManager manager = new BookingManager(
                paymentGateway,
                notificationService,
                eventRepository
        );

        String result = manager.book(request);

        assertEquals("TX123", result);

        verify(eventRepository).isSoldOut("E1");
        verify(paymentGateway).processPayment("C1", "E1", 3000);
        verify(eventRepository).saveBooking(request);
        verify(notificationService).sendConfirmation(request, "TX123");
    }
}
