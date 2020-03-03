package priv.cy.owner.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import priv.cy.owner.util.jwt.JwtProperties;
import priv.cy.owner.util.jwt.JwtUtil;
import priv.cy.owner.util.redis.RedisUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author ：cuiyang
 * @description：ShiroCache
 * @date ：Created in 2020/3/3 14:45
 */
public class ShiroCache<K, V> implements Cache<K, V> {
    private RedisUtil redisUtil;

    private JwtProperties jwtProperties;

    public ShiroCache(RedisUtil redisUtil, JwtProperties jwtProperties) {
        this.redisUtil = redisUtil;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public Object get(Object key) throws CacheException {
        String tempKey = this.getKey(key);
        Object result = null;
        if (redisUtil.hasKey(tempKey)) {
            result = redisUtil.get(tempKey);
        }
        return result;
    }

    /**
     * 保存缓存
     *
     * @param key
     * @param value
     * @return
     * @throws CacheException
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        String tempKey = this.getKey(key);
        redisUtil.set(tempKey, value, jwtProperties.getTokenExpireTime() * 60);
        return value;
    }

    /**
     * 移除缓存
     *
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public Object remove(Object key) throws CacheException {
        String tempKey = this.getKey(key);
        if (redisUtil.hasKey(tempKey)) {
            redisUtil.del(tempKey);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 20;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        Set keys = this.keys();
        List<V> values = new ArrayList<>();
//        try {
        for (Object key : keys) {
            values.add((V) redisUtil.get(this.getKey(key)));
        }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return values;
    }

    /**
     * 缓存的key名称获取为shiro:cache:account
     *
     * @param key
     * @return
     */
    private String getKey(Object key) {
        return JwtUtil.getUsername(key.toString());
    }
}
