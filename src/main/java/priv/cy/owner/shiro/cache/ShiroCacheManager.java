package priv.cy.owner.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.common.jwt.JwtProperties;
import priv.cy.owner.common.util.redis.RedisUtil;

/**
 * @author ：cuiyang
 * @description：shiro缓存
 * @date ：Created in 2020/3/3 14:43
 */
@Service
public class ShiroCacheManager implements CacheManager {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtProperties jwtProperties;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCache<K, V>(redisUtil, jwtProperties);
    }

}
