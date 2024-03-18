package com.example.movieclub.domain.comment.dto;

import com.example.movieclub.domain.comment.Comment;

import java.time.Duration;
import java.time.LocalDateTime;

public class CommentDtoMapper {
    public static CommentDto map(Comment comment) {
        Duration duration = Duration.between(comment.getDate(), LocalDateTime.now());
        String time;
        if (duration.getSeconds() < 60) {
            time = duration.getSeconds() + "s temu";
        } else if (duration.getSeconds() <= 3600) {
            time = duration.toMinutes() + "min temu";
        } else if (duration.getSeconds() <= 86400) {
            time = duration.toHours() + "g temu";
        } else if (duration.toDays() > 30) {
            time = duration.toDays() / 30 + "msc temu";
        } else if (duration.toDays() > 365) {
            time = duration.toDays() / 365 + "lat temu";
        } else {
            time = duration.toDays() + "dni temu";
        }
        return new CommentDto(
                comment.getId(),
                comment.getUserComment(),
                comment.getUser(),
                comment.getMovie().getId(),
                comment.getDate(),
                time
        );
    }
}
