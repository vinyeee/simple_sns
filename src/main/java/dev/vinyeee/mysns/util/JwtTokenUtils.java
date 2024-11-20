package dev.vinyeee.mysns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils{


    public static String getUserName(String token, String key){
         return extractClaims(token, key).get("userName", String.class);
    }

    // 유효성 검사를 위한 (= expired 됐는지 검사) private 매소드
    public static boolean isExpired(String token, String key){
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    // expired 됐는지를 알려면 claim 을 빼와야함
    // 클라이언트가 보낸 토큰의 header와 payload를 조합하여, 서버에서 보관 중인 key를 사용해 새로운 서명을 만듦
    // 이 새로 만든 서명과 토큰에 포함된 서명이 일치하면 유효한 토큰으로 간주하고, 이후 Claims를 추출
    public static Claims extractClaims(String token , String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key)) // Claim 을 가져올 parser 를 만들 준비하고 생성된 key 를 넣어줌
                .build().parseClaimsJws(token).getBody(); // parser 를 빌드하고 token 에서 payload 부분을 parse 함
    }


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
