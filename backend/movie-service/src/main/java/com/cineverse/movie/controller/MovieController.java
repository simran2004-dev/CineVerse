package com.cineverse.movie.controller;

import com.cineverse.movie.dto.ApiResponse;
import com.cineverse.movie.dto.MovieDTO;
import com.cineverse.movie.dto.ReviewDTO;
import com.cineverse.movie.entity.Movie;
import com.cineverse.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<ApiResponse<Movie>> createMovie(
            @Valid @RequestPart("movie") MovieDTO movieDTO,
            @RequestPart(value = "poster", required = false) MultipartFile poster) {
        Movie created = movieService.createMovie(movieDTO, poster);
        return new ResponseEntity<>(ApiResponse.success("Movie created successfully", created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Movie>>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "releaseDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {
        Page<Movie> movies = movieService.getAllMovies(page, size, sortBy, direction, title, genre);
        return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", movies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(ApiResponse.success("Movie retrieved successfully", movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> updateMovie(
            @PathVariable String id,
            @Valid @RequestBody MovieDTO movieDTO) {
        Movie updated = movieService.updateMovie(id, movieDTO);
        return ResponseEntity.ok(ApiResponse.success("Movie updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie deleted successfully", null));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<Movie>> addReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        Movie updatedMovie = movieService.addReview(id, reviewDTO);
        return ResponseEntity.ok(ApiResponse.success("Review added successfully", updatedMovie));
    }
}
