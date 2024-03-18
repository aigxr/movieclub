package com.example.movieclub.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
@Getter
@Setter
public class UserCredentialsSaveDto {
    private String email;
    private String password;
    private Set<String> roles;
    private String username;
    private MultipartFile profilePicture;
}
