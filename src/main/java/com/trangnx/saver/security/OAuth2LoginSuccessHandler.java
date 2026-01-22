package com.trangnx.saver.security;

import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${oauth.redirect-url}")
    private String oauthRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .fullName(name)
                            .googleId(googleId)
                            .avatarUrl(picture)
                            .provider(User.AuthProvider.GOOGLE)
                            .isActive(true)
                            .build();
                    return userRepository.save(newUser);
                });

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());

        // Redirect to configured URL (Flutter deep link or web frontend)
        String redirectUrl = String.format(
                "%s?token=%s&refreshToken=%s",
                oauthRedirectUrl,
                accessToken,
                refreshToken
        );

        System.out.println("DEBUG: OAuth redirect URL configured: " + oauthRedirectUrl);
        System.out.println("DEBUG: Redirecting to: " + redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}