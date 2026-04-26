package com.example.backendtemplate.Config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        template.setValueSerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.string());

        template.afterPropertiesSet();
        return template;
    }

    @Bean("redisObjectMapper")
    public ObjectMapper redisObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();
        mapper.deactivateDefaultTyping();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
