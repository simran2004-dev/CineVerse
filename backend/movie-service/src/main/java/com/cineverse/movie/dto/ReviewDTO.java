package com.cineverse.movie.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "User Name is required")
    private String userName;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private double rating;

    @NotBlank(message = "Comment cannot be empty")
    private String comment;
}
