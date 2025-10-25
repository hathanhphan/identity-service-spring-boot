package com.something.identity_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.something.identity_service.dto.request.AuthRequest;
import com.something.identity_service.dto.request.IntrospectRequest;
import com.something.identity_service.dto.request.LogoutRequest;
import com.something.identity_service.dto.response.AuthResponse;
import com.something.identity_service.dto.response.IntrospectResponse;
import com.something.identity_service.entity.InvalidatedToken;
import com.something.identity_service.entity.User;
import com.something.identity_service.repository.InvalidatedTokenRepository;
import com.something.identity_service.repository.UserRepository;
import com.something.identity_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Bad credentials");
        }
        String token = generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        verifyToken(token);
        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken());
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken(jit, expirationTime);
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified =  signedJWT.verify(verifier);
        if (!(verified && expirationTime.after(new Date()))) {
            throw new BadCredentialsException("Bad credentials");
        }
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        if (invalidatedTokenRepository.existsById(jit)) {
            throw new BadCredentialsException("Bad credentials");
        }
        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("something.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
           user.getRoles().forEach(role -> {
               stringJoiner.add("ROLE_" + role.getName());
               if (!role.getPermissions().isEmpty()) {
                   role.getPermissions().forEach(permission -> {
                       stringJoiner.add(permission.getName());
                   });
               }
           });
        }
        return stringJoiner.toString();
    }
}
