package com.cineverse.booking.repository;

import com.cineverse.booking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, String> {
    List<Show> findByMovieId(String movieId);
}
