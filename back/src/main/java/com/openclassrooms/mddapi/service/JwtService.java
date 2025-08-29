package com.openclassrooms.mddapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service responsible for generating JSON Web Tokens (JWT) for authenticated users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtEncoder jwtEncoder;

    /**
     * Generates a signed JWT token for the provided authentication information.
     *
     * @param authentication the Spring Security object representing the authenticated user
     * @return a JWT token
     * @throws RuntimeException if there is an error during token creation or encoding
     */
    public String generateToken(Authentication authentication) {
        try {
            Instant now = Instant.now();

            // Build token claims
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("mdd")
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .subject(authentication.getName())
                    .build();

            // Build encoder parameters with header and claims
            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS256).build(),
                    claims
            );

            // Generate and return the signed token
            return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }
}