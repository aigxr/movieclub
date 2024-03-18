package com.example.movieclub.domain.movie.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Getter
@Setter
public class MovieResponse {
    private int pageNo;
    private long totalElements;
    private int totalPages;
    private int nextPage;
    private int previousPage;
    private boolean last;
}
