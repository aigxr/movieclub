package com.example.movieclub.web;

import com.example.movieclub.domain.genre.GenreService;
import com.example.movieclub.domain.genre.dto.GenreDto;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieResponse;
import com.example.movieclub.domain.movie.dto.MovieResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final MovieService movieService;

    @GetMapping("/gatunek/{name}")
    public String getGenre(@PathVariable String name,
                           @RequestParam(required = false, defaultValue = "0") int page,
                           Model model) {
        PageRequest request = PageRequest.of(page, 10);
        GenreDto genre = genreService.findGenreByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<MovieDto> movies = movieService.findMoviesByGenreName(name, request);
        MovieResponse movieResponse = MovieResponseMapper.map(movies);
        model.addAttribute("heading", genre.getName());
        model.addAttribute("description", genre.getDescription());
        model.addAttribute("movies", movies); // by genre
        model.addAttribute("movieCount", movieResponse); //
        return "movie-listing";
    }

    @GetMapping("/gatunki-filmowe")
    public String getGenreList(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        return "genre-listing";
    }
}
