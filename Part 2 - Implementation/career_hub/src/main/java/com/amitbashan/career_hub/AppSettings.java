package com.amitbashan.career_hub;

import java.util.Base64;

public class AppSettings {
    public static final String AUTH_TOKEN_SECRET = "hello worldddasdasd hi hi hi hi hellooo";
    public static final String AUTH_TOKEN_SECRET_B64 = Base64.getEncoder().encodeToString(AUTH_TOKEN_SECRET.getBytes());;
    public static final int AUTH_TOKEN_VALID_DURATION_HOURS = 1;
}
