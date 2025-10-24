package com.something.identity_service.service;

import com.nimbusds.jose.JOSEException;
import com.something.identity_service.dto.request.AuthRequest;
import com.something.identity_service.dto.request.IntrospectRequest;
import com.something.identity_service.dto.response.AuthResponse;
import com.something.identity_service.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
