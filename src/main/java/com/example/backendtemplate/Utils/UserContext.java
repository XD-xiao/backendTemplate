package com.example.backendtemplate.Utils;

import java.util.Map;

public class UserContext {
    private static final ThreadLocal<Map<String, Object>> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(Map<String, Object> userClaims) {
        USER_THREAD_LOCAL.set(userClaims);
    }

    public static Map<String, Object> getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}