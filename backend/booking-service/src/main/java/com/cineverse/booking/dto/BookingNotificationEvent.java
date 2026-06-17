package com.cineverse.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingNotificationEvent implements Serializable {
    private String bookingId;
    private String userId;
    private String userEmail;
    private String userName;
    private String movieTitle;
    private String theatreName;
    private String showTime;
    private List<String> seatNumbers;
    private double totalAmount;
    private String bookingStatus;
}
