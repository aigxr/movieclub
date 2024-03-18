package com.example.movieclub.web;

import com.example.movieclub.config.security.CustomUserDetailsService;
import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import com.example.movieclub.domain.user.dto.UserCredentialsSaveDto;
import com.example.movieclub.web.admin.AdminController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = null;
        if (authentication.getName() != null)
            user = userService.findUserByEmail(authentication.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/settings")
    public String profileSettings() {
        return "profile-settings";
    }

    @PostMapping("/profile/username")
    public String changingUsername(UserCredentialsDto userDto, Authentication authentication, RedirectAttributes redirectAttributes) {
        userService.changeUsername(userDto, authentication);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Pomyślnie zmieniono nazwę użytkownika");
        return "redirect:/profile";
    }

    @PostMapping("/profile/email")
    public String changingEmail(@RequestParam String email, Authentication authentication, RedirectAttributes redirectAttributes) {
        customUserDetailsService.changeUsername(authentication.getName(), email);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE, "Pomyślnie zmieniono email. Zaloguj się ponownie."
        );
        return "redirect:/login?logout";
    }

    @PostMapping("/profile/password")
    public String changingPassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                   Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            customUserDetailsService.changePassword(authentication.getName(), oldPassword, newPassword);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                    AdminController.NOTIFICATION_ATTRIBUTE, "Niepoprawne hasło. Spróbuj ponownie."
            );
            return "redirect:/profile/settings";
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/profile/delete")
    public String deleteAccount(Authentication authentication, RedirectAttributes redirectAttributes) {
        userService.deleteUser(authentication.getName());
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE, "Pomyślnie usunięto konto."
        );
        return "redirect:/login?logout";
    }

    @PostMapping("/profile/picture")
    public String addProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        userService.saveImage(profilePicture, authentication);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE, "Pomyślnie zmieniono profilowe"
        );
        return "redirect:/profile";
    }
}
