package com.example.movieclub.web;

import com.example.movieclub.domain.rating.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/movie-rating")
    public String addMovieRating(@RequestParam long movieId,
                                 @RequestParam int rating,
                                 @RequestHeader String referer,
                                 Authentication authentication) {
        String currentUserEmail = authentication.getName();
        ratingService.addOrUpdateRating(currentUserEmail, movieId, rating);
        return "redirect:" + referer;
    }
}
