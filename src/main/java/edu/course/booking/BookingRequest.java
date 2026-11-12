package edu.course.booking;

public class BookingRequest {

    private String customerId;
    private String eventId;
    private int ticketQuantity;
    private int pricePerTicketInCents;

    public BookingRequest(String customerId, String eventId,
                          int ticketQuantity, int pricePerTicketInCents) {
        this.customerId = customerId;
        this.eventId = eventId;
        this.ticketQuantity = ticketQuantity;
        this.pricePerTicketInCents = pricePerTicketInCents;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEventId() {
        return eventId;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public int getPricePerTicketInCents() {
        return pricePerTicketInCents;
    }
}