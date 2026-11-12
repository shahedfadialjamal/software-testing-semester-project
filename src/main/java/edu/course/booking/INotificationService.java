package edu.course.booking;

public interface INotificationService {

    void sendConfirmation(BookingRequest booking, String transactionId);

}