package com.cineverse.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;

    private String title;
    private String description;
    private List<String> genre;
    private String language;
    private int durationMinutes;
    private LocalDate releaseDate;
    private String posterUrl;
    
    @Builder.Default
    private double averageRating = 0.0;
    
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
