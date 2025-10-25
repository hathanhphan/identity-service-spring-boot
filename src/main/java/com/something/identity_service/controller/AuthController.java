package com.something.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.something.identity_service.dto.request.AuthRequest;
import com.something.identity_service.dto.request.IntrospectRequest;
import com.something.identity_service.dto.request.LogoutRequest;
import com.something.identity_service.dto.response.ApiResponse;
import com.something.identity_service.dto.response.AuthResponse;
import com.something.identity_service.dto.response.IntrospectResponse;
import com.something.identity_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success("User logged in successfully", authResponse));
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = authService.introspect(request);
        return ResponseEntity.ok(ApiResponse.success(introspectResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("You have been logged out"));
    }
}
