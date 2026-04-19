package com.example.backendtemplate.Cache;

import com.example.backendtemplate.Pojo.TestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class TestCacheService {

    private static final String TEST_CACHE_KEY_PREFIX = "Test:"; // 用作前缀

    // TTL 范围：5~10 分钟（单位：秒）
    private static final int MIN_TTL_SECONDS = 300;
    private static final int MAX_TTL_SECONDS = 600;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存储 TestObject，并设置随机 TTL（每个 testId 独立过期）
     */
    public void set(TestObject testObject) {
        if (testObject == null || testObject.getTestId() == null) {
            throw new IllegalArgumentException("TestObject 或 testId 不能为 null");
        }

        String key = buildKey(testObject.getTestId());
        long randomTtl = ThreadLocalRandom.current().nextInt(MIN_TTL_SECONDS, MAX_TTL_SECONDS + 1);

        redisTemplate.opsForValue().set(key, testObject, randomTtl, TimeUnit.SECONDS);
    }


    public void setEmpty(Long testId) {
        String key = buildKey(testId);
        redisTemplate.opsForValue().set(key, "", 60, TimeUnit.SECONDS); // 空字符串或特殊标记
    }

    /**
     * 根据 testId 获取缓存对象
     */
    public TestObject get(Long testId) {
        if (testId == null) return null;
        String key = buildKey(testId);
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof TestObject) {
            return (TestObject) value;
        }
        // 如果担心类型问题，可以加日志或 fallback
        return null;
    }

    /**
     * 删除指定 testId 的缓存
     */
    public void delete(Long testId) {
        if (testId == null) {
            return;
        }
        String key = buildKey(testId);
        redisTemplate.delete(key);
    }

    /**
     * 构建 Redis key
     */
    private String buildKey(Long testId) {
        return TEST_CACHE_KEY_PREFIX + testId;
    }
}