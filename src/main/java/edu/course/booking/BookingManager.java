
package edu.course.booking;

public class BookingManager {

    private final IPaymentGateway paymentGateway;
    private final INotificationService notificationService;
    private final IEventRepository eventRepository;

    public BookingManager(
            IPaymentGateway paymentGateway,
            INotificationService notificationService,
            IEventRepository eventRepository) {

        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
        this.eventRepository = eventRepository;
    }

    public String book(BookingRequest request) {


        if (request == null ||
                request.getCustomerId() == null ||
                request.getCustomerId().isBlank() ||
                request.getEventId() == null ||
                request.getEventId().isBlank() ||
                request.getTicketQuantity() < 1 ||
                request.getTicketQuantity() > 5 ||
                request.getPricePerTicketInCents() <= 0) {

            throw new IllegalArgumentException("Invalid booking request");
        }


        if (eventRepository.isSoldOut(request.getEventId())) {
            throw new IllegalStateException("Event is sold out");
        }


        int totalAmount = request.getTicketQuantity()
                * request.getPricePerTicketInCents();


        String transactionId = paymentGateway.processPayment(
                request.getCustomerId(),
                request.getEventId(),
                totalAmount
        );


        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalStateException("Payment failed");
        }

        eventRepository.saveBooking(request);


        notificationService.sendConfirmation(request, transactionId);

        return transactionId;
    }
}
