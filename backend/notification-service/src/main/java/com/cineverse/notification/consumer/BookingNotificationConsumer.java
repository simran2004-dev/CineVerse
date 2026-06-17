package com.cineverse.notification.consumer;

import com.cineverse.notification.dto.BookingNotificationEvent;
import com.cineverse.notification.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingNotificationConsumer {

    private final EmailNotificationService emailNotificationService;

    @RabbitListener(queues = "${rabbitmq.queue.booking}")
    public void handleBookingEvent(BookingNotificationEvent event) {
        log.info("Received booking event: bookingId={}, status={}", event.getBookingId(), event.getBookingStatus());

        switch (event.getBookingStatus()) {
            case "CONFIRMED" -> {
                log.info("Processing CONFIRMED notification for user {}", event.getUserEmail());
                emailNotificationService.sendBookingConfirmation(event);
            }
            case "CANCELLED" -> {
                log.info("Processing CANCELLED notification for user {}", event.getUserEmail());
                emailNotificationService.sendBookingCancellation(event);
            }
            default -> log.warn("Unknown booking status received: {}", event.getBookingStatus());
        }
    }
}
