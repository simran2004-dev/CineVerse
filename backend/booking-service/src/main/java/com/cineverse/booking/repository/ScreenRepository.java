package com.cineverse.booking.repository;

import com.cineverse.booking.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, String> {
    List<Screen> findByTheatreId(String theatreId);
}
