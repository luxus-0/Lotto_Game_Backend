package integration.cache.redis.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheValue {

    @Autowired
    public static RedisTemplate<String, Object> redisTemplate;
    public static Object getCachedValue(String key, String hashKey) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }
}
