package com.cineverse.booking.service;

import com.cineverse.booking.dto.BookingRequestDTO;
import com.cineverse.booking.entity.*;
import com.cineverse.booking.exception.ResourceNotFoundException;
import com.cineverse.booking.repository.BookingRepository;
import com.cineverse.booking.repository.SeatRepository;
import com.cineverse.booking.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private ShowRepository showRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private SeatLockService seatLockService;
    @Mock private NotificationPublisherService notificationPublisherService;

    @InjectMocks
    private BookingService bookingService;

    private Screen screen;
    private Show show;
    private Seat seat1, seat2;
    private BookingRequestDTO request;

    @BeforeEach
    void setUp() {
        Theatre theatre = Theatre.builder()
                .id("theatre-1")
                .name("PVR Cinemas")
                .location("Mumbai")
                .ownerId("owner-1")
                .build();

        screen = Screen.builder()
                .id("screen-1")
                .name("Screen A")
                .capacity(100)
                .theatre(theatre)
                .build();

        show = Show.builder()
                .id("show-1")
                .movieId("movie-1")
                .screen(screen)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .priceMultiplier(1.0)
                .build();

        seat1 = Seat.builder().id("seat-1").row("A").column(1).type(SeatType.REGULAR).screen(screen).build();
        seat2 = Seat.builder().id("seat-2").row("A").column(2).type(SeatType.PREMIUM).screen(screen).build();

        request = new BookingRequestDTO();
        request.setUserId("user-1");
        request.setShowId("show-1");
        request.setSeatIds(List.of("seat-1", "seat-2"));
    }

    @Test
    void createBooking_Success() {
        when(showRepository.findById("show-1")).thenReturn(Optional.of(show));
        when(seatRepository.findAllById(any())).thenReturn(List.of(seat1, seat2));
        when(seatLockService.lockSeat(anyString(), anyString(), anyString())).thenReturn(true);
        when(bookingRepository.findByShowId(anyString())).thenReturn(new ArrayList<>());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.createBooking(request);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user-1");
        assertThat(result.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(result.getTotalAmount()).isEqualTo(250.0); // 100*1.0 + 100*1.5
    }

    @Test
    void createBooking_ShowNotFound_ThrowsException() {
        when(showRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Show not found");
    }

    @Test
    void createBooking_SeatAlreadyLocked_ThrowsException() {
        when(showRepository.findById("show-1")).thenReturn(Optional.of(show));
        when(seatRepository.findAllById(any())).thenReturn(List.of(seat1, seat2));
        when(seatLockService.lockSeat(anyString(), eq("seat-1"), anyString())).thenReturn(false);

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("locked by another user");
    }

    @Test
    void getBooking_NotFound_ThrowsException() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBooking("non-existent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }
}
