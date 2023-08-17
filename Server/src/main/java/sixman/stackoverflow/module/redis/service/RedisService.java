package sixman.stackoverflow.module.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveValues(String key, String authCode, Duration duration) {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, authCode, duration);
    }

    public String getValues(String key) {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    public void deleteValues(String key) {

            redisTemplate.delete(key);

    }
}
