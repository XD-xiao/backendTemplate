package com.example.backendtemplate.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class TestCacheService {

    private static final String SERVICE_CACHE_KEY_PREFIX = "BackendService:";
    private static final String NULL_SENTINEL = "__NULL__";
    private static final int NULL_TTL_SECONDS = 60;

    private static final int MIN_TTL_SECONDS = 300;
    private static final int MAX_TTL_SECONDS = 600;

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper redisObjectMapper;

    public TestCacheService(
            StringRedisTemplate stringRedisTemplate,
            @Qualifier("redisObjectMapper") ObjectMapper redisObjectMapper
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisObjectMapper = redisObjectMapper;
    }

    public <T> void set(String namespace, String id, T value) {
        String key = buildKey(namespace, id);
        long randomTtl = randomTtlSeconds(MIN_TTL_SECONDS, MAX_TTL_SECONDS);
        setObject(key, value, randomTtl, TimeUnit.SECONDS);
    }

    public <T> void set(String namespace, String id, T value, long ttl, TimeUnit timeUnit) {
        String key = buildKey(namespace, id);
        setObject(key, value, ttl, timeUnit);
    }

    public void setEmpty(String namespace, String id) {
        String key = buildKey(namespace, id);
        setNull(key, NULL_TTL_SECONDS, TimeUnit.SECONDS);
    }

    public <T> T get(String namespace, String id, Class<T> type) {
        String key = buildKey(namespace, id);
        return getObject(key, type);
    }

    public <T> T get(String namespace, String id, TypeReference<T> typeRef) {
        String key = buildKey(namespace, id);
        return getObject(key, typeRef);
    }

    public <T> T get(String namespace, String id, JavaType javaType) {
        String key = buildKey(namespace, id);
        return getObject(key, javaType);
    }

    public void delete(String namespace, String id) {
        delete(buildKey(namespace, id));
    }

    public <T> void setObject(String key, T value, long ttl, TimeUnit timeUnit) {
        validateKey(key);
        if (value == null) {
            throw new IllegalArgumentException("value 不能为 null");
        }
        if (ttl <= 0) {
            throw new IllegalArgumentException("ttl 必须 > 0");
        }
        String json;
        try {
            json = redisObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Redis 序列化失败", e);
        }
        stringRedisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    public void setNull(String key, long ttl, TimeUnit timeUnit) {
        validateKey(key);
        if (ttl <= 0) {
            throw new IllegalArgumentException("ttl 必须 > 0");
        }
        stringRedisTemplate.opsForValue().set(key, NULL_SENTINEL, ttl, timeUnit);
    }

    public <T> T getObject(String key, Class<T> type) {
        validateKey(key);
        String raw = stringRedisTemplate.opsForValue().get(key);
        if (raw == null || raw.isEmpty() || NULL_SENTINEL.equals(raw)) {
            return null;
        }
        try {
            return redisObjectMapper.readValue(raw, type);
        } catch (Exception e) {
            delete(key);
            return null;
        }
    }

    public <T> T getObject(String key, TypeReference<T> typeRef) {
        validateKey(key);
        String raw = stringRedisTemplate.opsForValue().get(key);
        if (raw == null || raw.isEmpty() || NULL_SENTINEL.equals(raw)) {
            return null;
        }
        try {
            return redisObjectMapper.readValue(raw, typeRef);
        } catch (Exception e) {
            delete(key);
            return null;
        }
    }

    public <T> T getObject(String key, JavaType javaType) {
        validateKey(key);
        String raw = stringRedisTemplate.opsForValue().get(key);
        if (raw == null || raw.isEmpty() || NULL_SENTINEL.equals(raw)) {
            return null;
        }
        try {
            return redisObjectMapper.readValue(raw, javaType);
        } catch (Exception e) {
            delete(key);
            return null;
        }
    }

    public void delete(String key) {
        validateKey(key);
        stringRedisTemplate.delete(key);
    }

    private String buildKey(String namespace, String id) {
        validateKeySegment(namespace, "namespace");
        validateKeySegment(id, "id");
        String key = SERVICE_CACHE_KEY_PREFIX + namespace + ":" + id;
        validateKey(key);
        return key;
    }

    private long randomTtlSeconds(int minSeconds, int maxSeconds) {
        if (minSeconds <= 0 || maxSeconds < minSeconds) {
            throw new IllegalArgumentException("TTL 范围非法");
        }
        return ThreadLocalRandom.current().nextLong(minSeconds, (long) maxSeconds + 1);
    }

    private void validateKeySegment(String segment, String name) {
        if (segment == null || segment.isBlank()) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
        if (segment.length() > 128) {
            throw new IllegalArgumentException(name + " 过长");
        }
        for (int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c) || c == ':') {
                throw new IllegalArgumentException(name + " 含非法字符");
            }
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key 不能为空");
        }
        if (key.length() > 512) {
            throw new IllegalArgumentException("key 过长");
        }
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("key 含非法字符");
            }
        }
    }
}
