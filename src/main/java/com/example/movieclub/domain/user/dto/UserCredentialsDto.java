package com.example.movieclub.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
@AllArgsConstructor
@Getter
public class UserCredentialsDto {
    private final Long id;
    private final String email;
    private final String password;
    private final Set<String> roles;
    private final String username;
    private final String profilePicture;
    private final boolean isBanned;
}
