
package edu.course.booking;

public interface IPaymentGateway {

    String processPayment(
            String customerId,
            String eventId,
            int amountInCents
    );

}
