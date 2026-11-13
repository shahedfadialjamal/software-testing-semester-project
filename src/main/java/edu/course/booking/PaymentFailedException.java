package edu.course.booking;

public class PaymentFailedException extends IllegalStateException {

    public PaymentFailedException(String message) {
        super(message);
    }
}