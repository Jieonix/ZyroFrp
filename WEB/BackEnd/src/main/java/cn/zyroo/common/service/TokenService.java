package cn.zyroo.common.service;

import cn.zyroo.common.dto.UserInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    private static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String generateToken(UserInfo userInfo) {
        String secretKey = generateSecretKey();

        return JWT.create()
                .withSubject(userInfo.getEmail())
                .withClaim("role", userInfo.getRole())
                .withClaim("user_key", userInfo.getUserKey())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getEmailFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("role").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getUserKeyFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("user_key").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.decode(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            return true;
        }
    }
}