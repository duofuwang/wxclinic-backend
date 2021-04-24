package com.dopoiv.clinic.common.utils;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/**
 * @author doverwong
 * @date 2021/4/24 13:17
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Data
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    private Date date;

    @Test
    @Order(1)
    public void generateToken() {
        setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvdS1mUzVnTXFiT0dWTkdHalBqcW1tNVZ2czlzIiwic2Vzc2lvbktleSI6IjBIVS92MDhscllkcmpxdHpNcjlISEE9PSIsIm9wZW5pZCI6Im91LWZTNWdNcWJPR1ZOR0dqUGpxbW01VnZzOXMiLCJleHAiOjE2MTkyNTYyNDR9.UbGv9oot6nyc0EIAvr0lLdF9DjF2M5mghAbqD8GuL-A5PlmlvRHD-EAqO0_Iz2szKRO0rGa5gDVQGiEVZdz2mg");
        System.out.println(getToken());

        Claims claims = jwtUtil.getClaimsFromToken(getToken());

        date = jwtUtil.getExpirationDateFromToken(getToken());

        boolean b = jwtUtil.isTokenExpired(getToken());

        Assertions.assertNotNull(getToken());
    }

    @Test
    @Order(2)
    @Disabled
    public void getExpirationDateFromToken() {
        date = jwtUtil.getExpirationDateFromToken(token);
        System.out.println(date);
        Assertions.assertNotNull(date);
    }

    @Test
    @Order(3)
    @Disabled
    public void isTokenExpired() {
        System.out.println(jwtUtil.isTokenExpired(token));
    }

    @Test
    @Order(4)
    @Disabled
    public void getClaimsFromToken() {
        System.out.println(getToken());
        Claims claims = jwtUtil.getClaimsFromToken(getToken());
        System.out.println(claims);
    }
}