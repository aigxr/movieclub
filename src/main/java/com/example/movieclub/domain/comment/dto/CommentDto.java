package com.example.movieclub.domain.comment.dto;

import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String userComment;
    private User user;
    private Long movieId;
    private LocalDateTime date;
    private String seconds;
}
