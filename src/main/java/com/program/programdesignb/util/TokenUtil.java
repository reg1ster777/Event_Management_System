package com.program.programdesignb.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {

    // 密钥（生产环境应该从配置文件读取）
    private static final String SECRET_KEY = "campus-activity-management-system-secret-key-2025";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成报名二维码token，有效期到报名截止时间
     */
    public String generateRegistrationToken(Integer activityId, Date signupEndTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("activityId", activityId);
        claims.put("type", "registration");

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(activityId))
                .issuedAt(new Date())
                .expiration(signupEndTime)
                .signWith(KEY)
                .compact();
    }

    /**
     * 生成签到二维码token，有效期为活动开始到结束
     */
    public String generateCheckinToken(Integer activityId, Date startTime, Date endTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("activityId", activityId);
        claims.put("type", "checkin");
        claims.put("startTime", startTime.getTime());

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(activityId))
                .issuedAt(new Date())
                .expiration(endTime)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析并验证token
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return false;
        }
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    /**
     * 从token中获取活动ID
     */
    public Integer getActivityIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("activityId", Integer.class);
    }

    /**
     * 从token中获取类型
     */
    public String getTokenType(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("type", String.class);
    }
}
