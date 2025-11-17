package cn.zyroo.common.service;

import cn.zyroo.common.dto.UserInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class TokenService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long tokenExpirationMs;

    public TokenService(
            @Value("${security.jwt.secret:}") String secret,
            @Value("${security.jwt.expiration-hours:24}") long expirationHours
    ) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured via security.jwt.secret");
        }
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(this.algorithm).build();
        this.tokenExpirationMs = Duration.ofHours(expirationHours).toMillis();
    }

    public String generateToken(UserInfo userInfo) {
        return JWT.create()
                .withSubject(userInfo.getEmail())
                .withClaim("role", userInfo.getRole())
                .withClaim("user_key", userInfo.getUserKey())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationMs))
                .sign(algorithm);
    }

    private DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }

    public String getEmailFromToken(String token) {
        try {
            return decodeToken(token).getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            return decodeToken(token).getClaim("role").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getUserKeyFromToken(String token) {
        try {
            return decodeToken(token).getClaim("user_key").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            return true;
        }
    }
}
