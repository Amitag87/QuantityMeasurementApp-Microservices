package com.apps.userservice.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apps.userservice.dto.AuthRequestDTO;
import com.apps.userservice.dto.AuthResponseDTO;
import com.apps.userservice.dto.RegisterRequestDTO;
import com.apps.userservice.entity.UserEntity;
import com.apps.userservice.exception.QuantityMeasurementException;
import com.apps.userservice.repository.UserRepository;
import com.apps.userservice.security.JwtUtil;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private final Set<String> invalidatedTokens = Collections.synchronizedSet(new HashSet<>());

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new QuantityMeasurementException("Email already exists");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole("ROLE_USER");

        userRepository.save(entity);

        String token = jwtUtil.generateToken(entity.getEmail(), entity.getRole());
        return new AuthResponseDTO(token);
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        String role = user.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("ROLE_USER");
        String token = jwtUtil.generateToken(user.getUsername(), role);
        return new AuthResponseDTO(token);
    }

    @Override
    public void logout(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }
}
