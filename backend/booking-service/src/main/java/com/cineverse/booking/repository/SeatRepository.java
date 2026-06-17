package com.cineverse.booking.repository;

import com.cineverse.booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {
    List<Seat> findByScreenId(String screenId);
}
