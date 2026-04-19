package com.example.backendtemplate.Utils;

import com.example.backendtemplate.Exception.TokenInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SIGNING_KEY_STRING = "NovelReadSystem-SecureSecretKey-2025!@#";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SIGNING_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    private static final Long EXPIRE = 100 * 43200000L; // 100 * 12 小时

    public static String getJWT(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT 字符串，失败时抛出 TokenInvalidException
     */
    public static Map<String, Object> parseJWT(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            throw new TokenInvalidException("令牌缺失");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException("令牌格式无效");
        } catch (ExpiredJwtException e) {
            throw new TokenInvalidException("令牌已过期");
        } catch (UnsupportedJwtException e) {
            throw new TokenInvalidException("令牌不支持");
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidException("令牌参数非法");
        } catch (SignatureException e) {
            throw new TokenInvalidException("令牌签名无效");
        } catch (Exception e) {
            throw new TokenInvalidException("令牌解析失败");
        }
    }
}