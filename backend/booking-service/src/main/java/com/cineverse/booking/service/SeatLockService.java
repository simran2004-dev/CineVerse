package com.cineverse.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    // Lock duration in minutes
    private static final long LOCK_TTL_MINUTES = 5;

    private String getLockKey(String showId, String seatId) {
        return "seat:lock:" + showId + ":" + seatId;
    }

    public boolean lockSeat(String showId, String seatId, String userId) {
        String key = getLockKey(showId, seatId);
        // setIfAbsent acts as a distributed lock (SETNX)
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, userId, Duration.ofMinutes(LOCK_TTL_MINUTES));
        return Boolean.TRUE.equals(success);
    }

    public boolean isSeatLocked(String showId, String seatId) {
        String key = getLockKey(showId, seatId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    public String getSeatLockOwner(String showId, String seatId) {
        String key = getLockKey(showId, seatId);
        Object owner = redisTemplate.opsForValue().get(key);
        return owner != null ? owner.toString() : null;
    }

    public void releaseSeat(String showId, String seatId, String userId) {
        String key = getLockKey(showId, seatId);
        String owner = getSeatLockOwner(showId, seatId);
        
        // Only the user who locked the seat can release it
        if (userId.equals(owner)) {
            redisTemplate.delete(key);
        }
    }
}
