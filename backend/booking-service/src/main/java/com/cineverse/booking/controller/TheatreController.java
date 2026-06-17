package com.cineverse.booking.controller;

import com.cineverse.booking.dto.ApiResponse;
import com.cineverse.booking.entity.Screen;
import com.cineverse.booking.entity.Show;
import com.cineverse.booking.entity.Theatre;
import com.cineverse.booking.repository.ScreenRepository;
import com.cineverse.booking.repository.ShowRepository;
import com.cineverse.booking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;

    @GetMapping
    @Cacheable(value = "theatres")
    public ResponseEntity<ApiResponse<List<Theatre>>> getAllTheatres() {
        return ResponseEntity.ok(ApiResponse.success("Theatres retrieved", theatreRepository.findAll()));
    }

    @PostMapping
    @CacheEvict(value = "theatres", allEntries = true)
    public ResponseEntity<ApiResponse<Theatre>> addTheatre(@RequestBody Theatre theatre) {
        Theatre saved = theatreRepository.save(theatre);
        return ResponseEntity.ok(ApiResponse.success("Theatre added", saved));
    }

    @GetMapping("/{id}/screens")
    @Cacheable(value = "screens", key = "#id")
    public ResponseEntity<ApiResponse<List<Screen>>> getScreensForTheatre(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Screens retrieved", screenRepository.findByTheatreId(id)));
    }

    @GetMapping("/shows/movie/{movieId}")
    @Cacheable(value = "shows", key = "#movieId")
    public ResponseEntity<ApiResponse<List<Show>>> getShowsForMovie(@PathVariable String movieId) {
        return ResponseEntity.ok(ApiResponse.success("Shows retrieved", showRepository.findByMovieId(movieId)));
    }
}
