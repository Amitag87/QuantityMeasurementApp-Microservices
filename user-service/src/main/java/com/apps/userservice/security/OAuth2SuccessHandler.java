package com.apps.userservice.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.apps.userservice.entity.UserEntity;
import com.apps.userservice.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.setRole("ROLE_USER");
            newUser.setAuthProvider("GOOGLE");
            return userRepository.save(newUser);
        });

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response,
                "http://localhost:4200/oauth2/callback?token=" + token);
    }
}
