package com.cineverse.booking.controller;

import com.cineverse.booking.dto.ApiResponse;
import com.cineverse.booking.dto.BookingRequestDTO;
import com.cineverse.booking.entity.Booking;
import com.cineverse.booking.entity.BookingStatus;
import com.cineverse.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        Booking booking = bookingService.createBooking(request);
        return new ResponseEntity<>(ApiResponse.success("Booking created successfully", booking), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBooking(@PathVariable String id) {
        Booking booking = bookingService.getBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getUserBookings(@PathVariable String userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(ApiResponse.success("User bookings retrieved successfully", bookings));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable String id,
            @RequestParam BookingStatus status) {
        Booking booking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated to " + status, booking));
    }
}
