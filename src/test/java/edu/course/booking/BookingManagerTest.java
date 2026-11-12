
package edu.course.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
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
    @ParameterizedTest
    @MethodSource("invalidRequests")
    void shouldRejectInvalidRequest(BookingRequest request) {

        BookingManager manager = new BookingManager(
                paymentGateway,
                notificationService,
                eventRepository
        );

        assertThrows(IllegalArgumentException.class,
                () -> manager.book(request));

        verifyNoInteractions(
                paymentGateway,
                notificationService,
                eventRepository
        );
    }

    static Stream<BookingRequest> invalidRequests() {
        return Stream.of(

                new BookingRequest(null, "E1", 2, 1500),

                new BookingRequest("", "E1", 2, 1500),

                new BookingRequest("C1", null, 2, 1500),

                new BookingRequest("C1", "", 2, 1500),

                new BookingRequest("C1", "E1", 0, 1500),

                new BookingRequest("C1", "E1", 6, 1500),

                new BookingRequest("C1", "E1", 2, 0),

                new BookingRequest("C1", "E1", 2, -100)
        );
    }
}
