package akhid.development.service;

import io.smallrye.jwt.build.Jwt;

import javax.inject.Singleton;

@Singleton
public class JwtService {
    public String generateJwt() {
        return Jwt.issuer("jwt")
                .subject("cake")
                .groups("writer").expiresAt(System.currentTimeMillis() + 3600).sign();
    }

    public String generateJwtAdmin() {
        return Jwt.issuer("jwt")
                .subject("cake")
                .groups("admin").expiresAt(System.currentTimeMillis() + 2800).sign();
    }
}
