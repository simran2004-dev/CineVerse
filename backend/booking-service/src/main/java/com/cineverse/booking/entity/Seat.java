package com.cineverse.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "seat_row", nullable = false)
    private String row;

    @Column(name = "seat_column", nullable = false)
    private int column;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
}
