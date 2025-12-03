package kspo.onfit.redis;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long ttl = 300L; // time_to_live

    //조회해서 없는 경우 삽입
    public void insert(String key, String value){
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
    }

    //캐시 조회
    public Optional<String> select(String key){
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value);
    }

    //캐시 무효화를 위함
    public void delete(String key){
        redisTemplate.delete(key);
    }

}
