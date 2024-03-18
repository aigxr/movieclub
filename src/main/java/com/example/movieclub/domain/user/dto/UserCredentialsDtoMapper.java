package com.example.movieclub.domain.user.dto;

import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        String username = user.getUsername();
        String profilePicture = user.getProfilePicture();
        boolean isBanned = user.isBanned();
        return new UserCredentialsDto(id, email, password, roles, username, profilePicture, isBanned);
    }
}
