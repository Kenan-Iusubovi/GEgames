package ge.games.gegames.service;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;

    private static final Duration EXPIRATION = Duration.ofMinutes(15);


    public void saveVerificationCode(String email, String code){

        String key = getKey(email);
        redisTemplate.opsForValue().set(key, code, EXPIRATION);
    }

    public boolean verifyCode(String email, String inputCode){

        String key = getKey(email);
        String storedCode = redisTemplate.opsForValue().get(key);

        if (!StringUtil.isNullOrEmpty(storedCode) && storedCode.equals(inputCode)){

            redisTemplate.delete(key);
            return true;
        }

        return false;
    }

    private String getKey(String email){

        return "verify:" + email;
    }
}
