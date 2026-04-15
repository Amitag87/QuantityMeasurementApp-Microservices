package com.apps.userservice.service;

import com.apps.userservice.dto.AuthRequestDTO;
import com.apps.userservice.dto.AuthResponseDTO;
import com.apps.userservice.dto.RegisterRequestDTO;

public interface IAuthService {
    AuthResponseDTO login(AuthRequestDTO request);
    AuthResponseDTO register(RegisterRequestDTO request);
    void logout(String token);
}
