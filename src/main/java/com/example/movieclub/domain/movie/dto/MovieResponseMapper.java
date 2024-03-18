package com.example.movieclub.domain.movie.dto;

import org.springframework.data.domain.Page;

public class MovieResponseMapper {
    public static MovieResponse map(Page<MovieDto> promotedMovies) {
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setPageNo(promotedMovies.getNumber());
        movieResponse.setTotalElements(promotedMovies.getTotalElements());
        movieResponse.setTotalPages(promotedMovies.getTotalPages());
        if (promotedMovies.hasNext()) {
            movieResponse.setNextPage(promotedMovies.getNumber() + 1);
        }
        movieResponse.setPreviousPage(promotedMovies.getNumber() - 1);
        movieResponse.setLast(promotedMovies.isLast());
        return movieResponse;
    }
}
