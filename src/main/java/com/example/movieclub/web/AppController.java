package com.example.movieclub.web;
import com.example.movieclub.domain.comment.Comment;
import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.comment.dto.CommentDto;
import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieResponse;
import com.example.movieclub.domain.movie.dto.MovieResponseMapper;
import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@Controller
@AllArgsConstructor
public class AppController {
    private final MovieService movieService;
    private final UserService userService;
    private final CommentService commentService;
    @GetMapping("/")
    public String home(@RequestParam(required = false) String title,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       Model model) {
        PageRequest request = PageRequest.of(page, 10);
        if (title != null) {
            Page<MovieDto> searchedMovies = movieService.searchMovies(title, request);
            MovieResponse movies = MovieResponseMapper.map(searchedMovies);
            model.addAttribute("heading", "Wyniki wyszukiwania");
            model.addAttribute("description", "Filmy znalezione dla: " + title);
            model.addAttribute("movies", movies);
        } else if (page >= 0) {
            Page<MovieDto> promotedMovies = movieService.findAllPromotedMoviesPaged(request);
            MovieResponse movieResponse = MovieResponseMapper.map(promotedMovies);
            if (movieResponse.getTotalPages() <= page) {
                return "error/404";
            }
            model.addAttribute("heading", "Promowane filmy");
            model.addAttribute("description", "Filmy polecane przez nasz zespół");
            model.addAttribute("movies", promotedMovies);
            model.addAttribute("movieCount", movieResponse);
        } else {
            return "error";
        }
        return "movie-listing";
    }

    @GetMapping("/profile/{username}")
    public String publicProfile(@PathVariable String username, Model model) {
        User user = userService.findUserByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "public-profile";
    }

//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<List<UserCredentialsDto>> searchUsers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
//        List<UserCredentialsDto> usersByKeyword = userService.findUsersByKeyword(keyword);
//        return ResponseEntity.ok(usersByKeyword);
//    }
}
