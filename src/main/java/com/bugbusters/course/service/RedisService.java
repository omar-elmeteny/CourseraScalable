package com.bugbusters.course.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    public void setValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateEntry(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {

            String oldValue = this.getValue(key);
            if (oldValue == null)
                return; // this means entry is not cached

            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
