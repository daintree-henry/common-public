package online.devwiki.common.oauth.domain.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import online.devwiki.common.oauth.jwt.JwtTokenDto;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String JWT_TOKEN_REDIS_KEY = "TOKEN";

    private final RedisTemplate<String, JwtTokenDto> redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void saveToken(JwtTokenDto jwtTokenDto) {
        if (hashOperations.hasKey(JWT_TOKEN_REDIS_KEY, jwtTokenDto.getUserId()))
            hashOperations.delete(JWT_TOKEN_REDIS_KEY, jwtTokenDto.getUserId());
        hashOperations.put(JWT_TOKEN_REDIS_KEY, jwtTokenDto.getUserId(), jwtTokenDto.getToken());
    }

    public JwtTokenDto findToken(String loginId) {
        return (JwtTokenDto) hashOperations.get(JWT_TOKEN_REDIS_KEY, loginId);
    }
}

