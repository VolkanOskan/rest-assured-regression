package com.testproject.utils;

public final class ApiConfig {
    private ApiConfig() {}
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String TODOS_ENDPOINT = "/todos";
    public static final long MAX_RESPONSE_TIME_MS = 3000L;
    public static final long FAST_RESPONSE_TIME_MS = 1500L;
    public static final int EXISTING_POST_ID = 1;
    public static final int NON_EXISTING_ID = 99999;
}