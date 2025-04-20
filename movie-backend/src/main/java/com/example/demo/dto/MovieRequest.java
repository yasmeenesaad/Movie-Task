package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieRequest {
    private String title;
    private String year;
    private String genre;
    private String director;
    private String plot;
    private String poster;
    private String imdbId;
}
