package com.gestorcolegio.service;

import com.gestorcolegio.dto.AuthRequest;
import com.gestorcolegio.dto.AuthResponse;
import com.gestorcolegio.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
