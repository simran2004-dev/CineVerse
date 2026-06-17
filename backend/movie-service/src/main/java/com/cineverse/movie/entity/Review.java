package com.cineverse.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private String userId;
    private String userName;
    private double rating;
    private String comment;
    private LocalDateTime createdAt;
}
