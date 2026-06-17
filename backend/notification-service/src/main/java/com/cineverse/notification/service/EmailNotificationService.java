package com.cineverse.notification.service;

import com.cineverse.notification.dto.BookingNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(BookingNotificationEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getUserEmail());
            message.setSubject("🎬 CineVerse — Booking Confirmed! [#" + event.getBookingId() + "]");
            message.setText(buildConfirmationBody(event));

            mailSender.send(message);
            log.info("Confirmation email sent to {} for booking {}", event.getUserEmail(), event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to send email to {} — reason: {}", event.getUserEmail(), e.getMessage());
        }
    }

    public void sendBookingCancellation(BookingNotificationEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getUserEmail());
            message.setSubject("❌ CineVerse — Booking Cancelled [#" + event.getBookingId() + "]");
            message.setText(buildCancellationBody(event));

            mailSender.send(message);
            log.info("Cancellation email sent to {} for booking {}", event.getUserEmail(), event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to send cancellation email — reason: {}", e.getMessage());
        }
    }

    private String buildConfirmationBody(BookingNotificationEvent event) {
        return String.format("""
                Hi %s,

                Your booking has been CONFIRMED! Here are your details:

                🎬 Movie      : %s
                🏟️  Theatre    : %s
                🕐 Show Time  : %s
                💺 Seats      : %s
                💳 Total Paid : ₹%.2f

                Booking ID: %s

                Enjoy your movie! 🍿
                
                — CineVerse Team
                """,
                event.getUserName(),
                event.getMovieTitle(),
                event.getTheatreName(),
                event.getShowTime(),
                String.join(", ", event.getSeatNumbers()),
                event.getTotalAmount(),
                event.getBookingId()
        );
    }

    private String buildCancellationBody(BookingNotificationEvent event) {
        return String.format("""
                Hi %s,

                Your booking #%s has been CANCELLED.

                🎬 Movie      : %s
                💺 Seats      : %s
                💳 Refund     : ₹%.2f (processing in 3–5 business days)

                If you have questions, contact support@cineverse.com.

                — CineVerse Team
                """,
                event.getUserName(),
                event.getBookingId(),
                event.getMovieTitle(),
                String.join(", ", event.getSeatNumbers()),
                event.getTotalAmount()
        );
    }
}
