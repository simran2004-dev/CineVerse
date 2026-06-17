package com.cineverse.booking.service;

import com.cineverse.booking.config.RabbitMQConfig;
import com.cineverse.booking.dto.BookingNotificationEvent;
import com.cineverse.booking.entity.Booking;
import com.cineverse.booking.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publishBookingEvent(Booking booking, String movieTitle, String theatreName) {
        List<String> seatNumbers = booking.getSeats().stream()
                .map(seat -> seat.getRow() + seat.getColumn())
                .toList();

        BookingNotificationEvent event = BookingNotificationEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .userEmail("user-" + booking.getUserId() + "@cineverse.com") // Placeholder: In production, fetch from auth-service
                .userName("User " + booking.getUserId())
                .movieTitle(movieTitle)
                .theatreName(theatreName)
                .showTime(booking.getShow().getStartTime().toString())
                .seatNumbers(seatNumbers)
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getStatus().name())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.BOOKING_ROUTING_KEY,
                event
        );

        log.info("Published booking event for bookingId={}, status={}", booking.getId(), booking.getStatus());
    }
}
