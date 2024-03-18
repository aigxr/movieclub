package com.example.movieclub.web;

import com.example.movieclub.domain.comment.Comment;
import com.example.movieclub.domain.comment.dto.CommentDtoMapper;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieResponse;
import com.example.movieclub.domain.movie.dto.MovieResponseMapper;
import com.example.movieclub.domain.rating.RatingService;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.comment.dto.CommentDto;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final RatingService ratingService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/film/{id}")
    public String getMovie(@PathVariable long id, Model model, Authentication authentication) {
        List<CommentDto> allComments = commentService.findAllCommentsByMovieId(id);
        model.addAttribute("comments", allComments);
        MovieDto movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("movie", movie);
        // jeżeli zalogowany
        if (authentication != null) {
            String currectUserEmail = authentication.getName();
            // wyszukujemy jego glos z bazy
            Integer rating = ratingService.getUserRatingForMovie(currectUserEmail, id).orElse(0);
            model.addAttribute("userRating", rating);
            UserCredentialsDto user = userService.findCredentialsByEmail(currectUserEmail).orElseThrow();
            model.addAttribute("user", user);
        }
        return "movie";
    }

    @GetMapping("/top10")
    public String findTop10(Model model) {
        List<MovieDto> topMovies = movieService.findTopMovies(10);
        model.addAttribute("heading", "Filmowe TOP10");
        model.addAttribute("description", "Filmy najlepiej oceniane przez użytkowników");
        model.addAttribute("movies", topMovies);
        return "movie-listing";
    }

    @GetMapping("/all-movies")
    public String allMovies(@RequestParam(required = false, defaultValue = "0") int page, Model model) {
        PageRequest request = PageRequest.of(page, 10);
        Page<MovieDto> allMovies = movieService.findAllMovies(request); // proszac o dane na strone ZAWSZE musi byc DTO.....
        MovieResponse movieResponse = MovieResponseMapper.map(allMovies);
        model.addAttribute("heading", "Wszystkie filmy");
        model.addAttribute("description", "Lista wszystkich filmów");
        model.addAttribute("movies", allMovies);
        model.addAttribute("movieCount", movieResponse);
        return "movie-listing";
    }
}
