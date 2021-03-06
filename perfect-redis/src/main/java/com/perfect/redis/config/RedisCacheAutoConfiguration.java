package com.perfect.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfect.common.properies.PerfectConfigProperies;
import com.perfect.redis.listener.RedisKeyExpirationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Slf4j
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

    @Autowired
    RedisKeyExpirationListener redisKeyExpirationListener;
    @Autowired
    private PerfectConfigProperies perfectConfigProperies;

    /**
     * ????????????????????????
     * ???????????????????????????key????????????????????????
     *
     * @param lettuceConnectionFactory
     * @param jackson2JsonRedisSerializer
     * @return
     */
    @Bean
    public CacheManager cacheManager(
        @Qualifier("perfect_lettuce_connection_factory") LettuceConnectionFactory lettuceConnectionFactory,
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {

        RedisSerializationContext.SerializationPair keyPair =
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
        RedisSerializationContext.SerializationPair valuePair =
            RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);

        //???????????????????????????RedisCacheWriter??????
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory);

        int redisCacheExpiredMin = perfectConfigProperies.getRedisCacheExpiredMin();
        RedisCacheConfiguration cacheConfiguration =
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(redisCacheExpiredMin)).serializeKeysWith(keyPair)
                .serializeValuesWith(valuePair);

        RedisCacheManager redisCacheManager = new RedisCacheManager(writer, cacheConfiguration);

        return redisCacheManager;

    }

    /**
     * ?????????????????????, ????????????????????????????????????, ?????????????????????????????????
     * ????????????cacheManager???redisTemplate
     */
    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    /**
     * redisTemplate ???????????????value ??????Jackson2JsonRedisSerializer
     *
     * @param lettuceConnectionFactory
     * @param jackson2JsonRedisSerializer
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(
        @Qualifier("perfect_lettuce_connection_factory") LettuceConnectionFactory lettuceConnectionFactory,
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // ????????????AutoType??????????????????
        // ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // ???????????????????????????????????????????????????
        ParserConfig.getGlobalInstance().addAccept("com.perfect.");

        // ????????????key?????????????????????StringRedisSerializer???
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // ????????????value?????????????????????FastJsonRedisSerializer???
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // Hash key?????????
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash value?????????
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public ChannelTopic expiredTopic() {
        ChannelTopic ot = new ChannelTopic("__keyevent@0__:expired");
        return ot;
    }

    /**
     * redis????????????
     * @param
     * @return
     */
    @Bean("perfect_redis_listener_container")
    RedisMessageListenerContainer keyExpirationListenerContainer(
        @Qualifier("perfect_lettuce_connection_factory") LettuceConnectionFactory lettuceConnectionFactory) {

        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(lettuceConnectionFactory);
        return listenerContainer;
    }

    /**
     * ????????????????????????redis??????????????????????????????????????????????????????????????????
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {

        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                e.printStackTrace();
                log.error("??????key:???{}???????????????:{}", key, e);

            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {

                e.printStackTrace();
                log.error("??????key:???{}?????????????????????:{}", key, e);

            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {

                e.printStackTrace();
                log.error("??????key:???{}????????????????????????:{}", key, e);

            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {

                e.printStackTrace();
                log.error("????????????????????????:{}", e);

            }
        };
        return cacheErrorHandler;
    }

}
