package dev.vinyeee.mysns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils{

    public static String generateToken(String userName, String key, long expiredTimeMs){
        // userName 을 이용하여 JWT 토큰 생성
        Claims claims = Jwts.claims();
        claims.put("userName",userName);

        // 토큰 리턴
        return Jwts.builder()
                .setClaims(claims) // userName을 이용해만든 claim
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs)) // 토큰 파기 시간
                .signWith(getKey(key), SignatureAlgorithm.HS256) // 암호화 key와 알고리즘
                .compact();

    }

    private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
