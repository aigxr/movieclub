package com.example.movieclub.web.admin;

import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.genre.GenreService;
import com.example.movieclub.domain.genre.dto.GenreDto;
import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieSaveDto;
import com.example.movieclub.domain.rating.RatingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class MovieManagementController {
    private final MovieService movieService;
    private final GenreService genreService;
    private final RatingService ratingService;
    private final CommentService commentService;

    @GetMapping("/admin/dodaj-film")
    public String addMovieForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        MovieSaveDto movie = new MovieSaveDto();
        model.addAttribute("movie", movie);
        return "admin/movie-form";
    }

    @PostMapping("/admin/dodaj-film")
    public String addMovie(MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.addMovie(movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Film %s został zapisany".formatted(movie.getTitle()));
        return "redirect:/admin";
    }

    @GetMapping("/admin/update-movie/{id}")
    public String updateMovieForm(@PathVariable Long id, Model model) {
        MovieDto movie = movieService.findMovieById(id).orElseThrow();
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", allGenres);
        return "admin/update-movie";
    }

    @PostMapping("/admin/update-movie/{id}")
    public String updateMovieById(@PathVariable Long id, MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        MovieDto movieToUpdate = movieService.findMovieById(id).orElseThrow();
        movieService.updateMovieById(id, movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Film %s został zaaktualizowany".formatted(movieToUpdate.getTitle())
        );
        return "redirect:/admin";
    }

    @GetMapping("/admin/manage-movie")
    public String deleteMovieTable(Model model) {
        List<MovieDto> allMovies = movieService.findAllMovies();
        model.addAttribute("movies", allMovies);
        return "admin/manage-movie";
    }

    @Transactional
    @PostMapping("/admin/manage-movie/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MovieDto movie = movieService.findMovieById(id).orElseThrow();
        ratingService.deleteRatingByMovieId(id);
        commentService.deleteCommentByMovieId(id);
        movieService.deleteMovie(id);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Film %s został usunięty z bazy danych".formatted(movie.getTitle())
        );
        return "redirect:/admin";
    }
}
