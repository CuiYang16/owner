package priv.cy.owner.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis自定义配置
 */
@Configuration
@EnableCaching
public class RedisConfig {

    // volatile 修饰
    private static volatile JedisPool jedisPool = null;

    public static JedisPool getJedisPoolInstance() {
        // 使用双重检查创建单例
        if (null == jedisPool) {
            synchronized (RedisConfig.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(10);
                    poolConfig.setMaxIdle(10);
                    poolConfig.setMinIdle(2);
                    poolConfig.setMaxWaitMillis(30 * 1000);
                    poolConfig.setTestOnBorrow(true);
                    poolConfig.setTestOnReturn(true);
                    poolConfig.setTimeBetweenEvictionRunsMillis(10 * 1000);
                    poolConfig.setMinEvictableIdleTimeMillis(30 * 1000);
                    poolConfig.setNumTestsPerEvictionRun(-1);
                    jedisPool = new JedisPool(poolConfig, "39.108.254.80", 6379, 360000, "my_redis");
                }
            }
        }
        return jedisPool;
    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
