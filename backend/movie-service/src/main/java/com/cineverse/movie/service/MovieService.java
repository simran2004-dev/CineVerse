package com.cineverse.movie.service;

import com.cineverse.movie.dto.MovieDTO;
import com.cineverse.movie.dto.ReviewDTO;
import com.cineverse.movie.entity.Movie;
import com.cineverse.movie.entity.Review;
import com.cineverse.movie.exception.ResourceNotFoundException;
import com.cineverse.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final FileStorageService fileStorageService;

    public Movie createMovie(MovieDTO dto, MultipartFile poster) {
        String posterUrl = null;
        if (poster != null && !poster.isEmpty()) {
            posterUrl = fileStorageService.storeFile(poster);
        }

        Movie movie = Movie.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .genre(dto.getGenre())
                .language(dto.getLanguage())
                .durationMinutes(dto.getDurationMinutes())
                .releaseDate(dto.getReleaseDate())
                .posterUrl(posterUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return movieRepository.save(movie);
    }

    public Page<Movie> getAllMovies(int page, int size, String sortBy, String direction, String title, String genre) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (title != null && !title.isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (genre != null && !genre.isEmpty()) {
            return movieRepository.findByGenreContainingIgnoreCase(genre, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    public Movie updateMovie(String id, MovieDTO dto) {
        Movie movie = getMovieById(id);
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setGenre(dto.getGenre());
        movie.setLanguage(dto.getLanguage());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setUpdatedAt(LocalDateTime.now());
        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id " + id);
        }
        movieRepository.deleteById(id);
    }

    public Movie addReview(String movieId, ReviewDTO reviewDTO) {
        Movie movie = getMovieById(movieId);

        Review review = Review.builder()
                .userId(reviewDTO.getUserId())
                .userName(reviewDTO.getUserName())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        movie.getReviews().add(review);
        movie.setAverageRating(calculateAverageRating(movie));
        movie.setUpdatedAt(LocalDateTime.now());
        return movieRepository.save(movie);
    }

    private double calculateAverageRating(Movie movie) {
        if (movie.getReviews() == null || movie.getReviews().isEmpty()) {
            return 0.0;
        }
        double sum = movie.getReviews().stream().mapToDouble(Review::getRating).sum();
        return Math.round((sum / movie.getReviews().size()) * 10.0) / 10.0;
    }
}
