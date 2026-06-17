package com.cineverse.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;

    @NotNull(message = "Genre is mandatory")
    private List<String> genre;

    @NotBlank(message = "Language is mandatory")
    private String language;

    @Positive(message = "Duration must be positive")
    private int durationMinutes;

    private LocalDate releaseDate;
}
