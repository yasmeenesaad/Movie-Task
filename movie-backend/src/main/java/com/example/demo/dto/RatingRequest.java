package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private Long movieId;
    private int rating; // Value between 1-5
}
