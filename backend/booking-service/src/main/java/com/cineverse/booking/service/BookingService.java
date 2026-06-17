package com.cineverse.booking.service;

import com.cineverse.booking.dto.BookingRequestDTO;
import com.cineverse.booking.entity.Booking;
import com.cineverse.booking.entity.BookingStatus;
import com.cineverse.booking.entity.Seat;
import com.cineverse.booking.entity.Show;
import com.cineverse.booking.exception.ResourceNotFoundException;
import com.cineverse.booking.repository.BookingRepository;
import com.cineverse.booking.repository.SeatRepository;
import com.cineverse.booking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final SeatLockService seatLockService;
    private final NotificationPublisherService notificationPublisherService;

    @Transactional
    public Booking createBooking(BookingRequestDTO request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<Seat> selectedSeats = seatRepository.findAllById(request.getSeatIds());
        if (selectedSeats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats not found");
        }

        // Validate that seats belong to the correct screen
        boolean allSeatsValid = selectedSeats.stream()
                .allMatch(seat -> seat.getScreen().getId().equals(show.getScreen().getId()));
        
        if (!allSeatsValid) {
            throw new IllegalStateException("Selected seats do not belong to the show's screen");
        }

        // Try to acquire distributed locks in Redis
        for (String seatId : request.getSeatIds()) {
            if (!seatLockService.lockSeat(show.getId(), seatId, request.getUserId())) {
                throw new IllegalStateException("Seat " + seatId + " is currently locked by another user");
            }
        }

        // Check if seats are already booked in database
        List<Booking> existingBookings = bookingRepository.findByShowId(show.getId());
        for (Booking existingBooking : existingBookings) {
            if (existingBooking.getStatus() != BookingStatus.CANCELLED && existingBooking.getStatus() != BookingStatus.FAILED) {
                for (Seat bookedSeat : existingBooking.getSeats()) {
                    if (request.getSeatIds().contains(bookedSeat.getId())) {
                        throw new IllegalStateException("Seat " + bookedSeat.getRow() + bookedSeat.getColumn() + " is already booked");
                    }
                }
            }
        }

        // Calculate total amount (Base price logic - simplified here, can depend on SeatType)
        double basePrice = 100.0; // Base ticket price
        double totalAmount = 0.0;
        
        for (Seat seat : selectedSeats) {
            double seatMultiplier = switch (seat.getType()) {
                case VIP -> 2.0;
                case PREMIUM -> 1.5;
                case REGULAR -> 1.0;
            };
            totalAmount += basePrice * seatMultiplier * show.getPriceMultiplier();
        }

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .show(show)
                .seats(selectedSeats)
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING) // Pending until payment is confirmed
                .bookingTime(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }

    public Booking getBooking(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public Booking updateBookingStatus(String bookingId, BookingStatus status) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(status);
        Booking saved = bookingRepository.save(booking);

        // Publish async notification event when booking is confirmed or cancelled
        if (status == BookingStatus.CONFIRMED || status == BookingStatus.CANCELLED) {
            try {
                String movieTitle = "Movie-" + saved.getShow().getMovieId(); // Placeholder: real impl fetches from movie-service
                String theatreName = saved.getShow().getScreen().getTheatre().getName();
                notificationPublisherService.publishBookingEvent(saved, movieTitle, theatreName);
            } catch (Exception e) {
                log.warn("Failed to publish notification event for booking {}: {}", bookingId, e.getMessage());
            }
        }
        return saved;
    }
}
