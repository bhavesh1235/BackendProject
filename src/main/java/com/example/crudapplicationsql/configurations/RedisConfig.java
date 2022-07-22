package com.example.crudapplicationsql.configurations;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class RedisConfig {

        @Bean
        JedisConnectionFactory jedisConnectionFactory() {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
            jedisPoolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
            jedisPoolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWait(Duration.ofMinutes(5));
            return new JedisConnectionFactory(redisStandaloneConfiguration , JedisClientConfiguration.builder().usePooling()
                    .poolConfig(jedisPoolConfig)
                    .build());
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setConnectionFactory(jedisConnectionFactory());
            template.afterPropertiesSet();
            return template;
        }

}
