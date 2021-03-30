package com.yh.common.redis.impl;

import com.yh.common.redis.RedisHystrixCommand;
import com.yh.common.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import javax.annotation.Resource;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis缓存服务实现
 */
public class RedisServiceImpl implements RedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public TreeMap<Long, String> slotHostMap = null;

    public Map<String, JedisPool> nodeMap = null;

    public JedisCluster jedisCluster;

    Map<String, String> locks = new ConcurrentHashMap<>();

    /**
     * 获取object lock
     *
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        if (key == null) {
            LOGGER.error(" ================the key can not be null");
            return false;
        }
        String putIfAbsent = locks.putIfAbsent(key, key);
        if (putIfAbsent == null) {
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key
     */
    private void releaseLock(String key) {
        if (key != null) {
            locks.remove(key);
        }
    }

    @Override
    public DataType getkeyType(String key) {
        return executeGetInHystrix(() -> {
            long start1 = Clock.systemUTC().millis();
            DataType type = redisTemplate.type(key);
            long end1 = Clock.systemUTC().millis();
            LOGGER.debug(Thread.currentThread().getName() + "线程================耗时:" + (end1 - start1) + "毫秒");
            return type;
        }, "redis.impl.RedisServiceImpl.getkeyType");
    }

    @Override
    public <T> void set(String key, T value) {
        try {
            if (tryLock(key)) {
                executePutInHystrix(() -> {
                    redisTemplate.opsForValue().set(key, value);
                    return null;
                }, "redis.impl.RedisServiceImpl.set(java.lang.String, T)");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseLock(key);
        }
    }

    @Override
    public <T> void set(byte[] key, byte[] value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> void set(String key, T value, Long timeout) {
        try {
            if (tryLock(key)) {
                executePutInHystrix(() -> {
                    redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
                    return null;
                }, "redis.impl.RedisServiceImpl.set(java.lang.String, T, java.lang.Long)");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseLock(key);
        }
    }

    @Override
    public <T> void setPipelined(Map<String, T> map, Long timeout) {
        if (map == null || map.isEmpty()) {
            return;
        }

        Map<String, List<String>> jedisMap = map.keySet().stream().collect(Collectors.groupingBy(this::getJedisByKey));
        RedisSerializer keySerializer = getKeySerializer();
        RedisSerializer redisSerializer = getValueSerializer();
        jedisMap.keySet().stream().forEach(jedisHostStr -> {
            if (!"".equals(jedisHostStr)) {
                Jedis jedis = nodeMap.get(jedisHostStr).getResource();
                try {
                    Pipeline p = jedis.pipelined();
                    jedisMap.get(jedisHostStr).stream().forEach(key -> {
                        p.set(keySerializer.serialize(key), redisSerializer.serialize(map.get(key)));
                        p.expire(key, timeout.intValue());
                    });
                    p.sync();
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            }
        });

    }

    @Override
    public <HK, T> HashOperations<String, HK, T> opsForHash() {
        return redisTemplate.opsForHash();
    }

    @Override
    public <T> ZSetOperations<String, T> opsForZSet() {
        return redisTemplate.opsForZSet();
    }

    @Override
    public <T> Boolean setIfAbsent(String key, T value) {
        try {
            if (tryLock(key)) {
                return executeGetInHystrix(() -> {
                    boolean isSucc = false;
                    try {
                        BoundValueOperations<?, T> boundValueOperations = redisTemplate.boundValueOps(key);
                        isSucc = boundValueOperations.setIfAbsent(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return isSucc;
                }, "redis.impl.RedisServiceImpl.setIfAbsent(java.lang.String, T)");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseLock(key);
        }
        return false;
    }

    @Override
    public <T> Boolean setIfAbsent(String key, T value, long unixTime) {
        try {
            if (tryLock(key)) {
                return executeGetInHystrix(() -> {
                    boolean isSucc = false;
                    try {
                        BoundValueOperations<?, T> boundValueOperations = redisTemplate.boundValueOps(key);
                        isSucc = boundValueOperations.setIfAbsent(value);
                        if (isSucc) {
                            boundValueOperations.expire(unixTime, TimeUnit.MILLISECONDS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return isSucc;
                }, "redis.impl.RedisServiceImpl.setIfAbsent(java.lang.String, T, long)");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseLock(key);
        }
        return false;
    }

    @Override
    public <T> T get(String key) {
        try {
            return executeGetInHystrix(() -> (T) redisTemplate.opsForValue().get(key),
                    "redis.impl.RedisServiceImpl.get");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getOne(String key) {
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> List<T> getPipelined(List<String> keyList) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(keyList)) {
            return list;
        }

        RedisSerializer redisSerializer = getValueSerializer();
        Map<String, List<String>> jedisMap = keyList.stream().collect(Collectors.groupingBy(this::getJedisByKey));
        jedisMap.keySet().stream().forEach(jedisHostStr -> {

            Jedis jedis = nodeMap.get(jedisHostStr).getResource();
            try {

                Pipeline p = jedis.pipelined();

                jedisMap.get(jedisHostStr).stream().forEach(key -> {
                    p.get(key);
                });
                List<Object> pList = p.syncAndReturnAll();

                if (!CollectionUtils.isEmpty(pList)) {
                    list.addAll(pList.stream().map(obj -> {

                        if (obj == null) {
                            return null;
                        }
                        String objStr = (String) obj;
                        return (T) redisSerializer.deserialize(objStr.getBytes());
                    }).collect(Collectors.toList()));
                }
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }

        });

        return list;
    }

    @Override
    public boolean expireInSeconds(String key, int seconds) {
        return executeGetInHystrix(() -> redisTemplate.expire(key, seconds, TimeUnit.SECONDS),
                "redis.impl.RedisServiceImpl.expireInSeconds");
    }


    @Override
    public boolean expireInMilliSeconds(String key, long milliSeconds) {
        return executeGetInHystrix(() -> redisTemplate.expire(key, milliSeconds, TimeUnit.MILLISECONDS),
                "redis.impl.RedisServiceImpl.expireInMilliSeconds");
    }

    @Override
    public Long getExpire(String key) {
        return executeGetInHystrix(() -> redisTemplate.getExpire(key),
                "redis.impl.RedisServiceImpl.getExpire");
    }

    @Override
    public <T> void addMap(T key, T field, T value, long time) {
        redisTemplate.boundHashOps(key).put(field, value);
        redisTemplate.boundHashOps(key).expire(time, TimeUnit.MILLISECONDS);
    }


    @Override
    public void del(String key) {
        executePutInHystrix(() -> {
            redisTemplate.delete(key);
            return null;
        }, "redis.impl.RedisServiceImpl.del");
        redisTemplate.delete(key);
    }

    @Override
    public void sDel(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        return executeGetInHystrix(() -> redisTemplate.keys(pattern),
                "redis.impl.RedisServiceImpl.keys");
    }

    @Override
    public long listlength(String key) {
        return executeGetInHystrix(() -> redisTemplate.opsForList().size(key),
                "redis.impl.RedisServiceImpl.listlength");
    }

    @Override
    public <T> long listLeftPush(String key, T value) {
        return executeGetInHystrix(() -> redisTemplate.opsForList().leftPush(key, value),
                "redis.impl.RedisServiceImpl.listLeftPush");
    }

    @Override
    public void trim(String key, long start, long end) {
        executePutInHystrix(() -> {
            redisTemplate.opsForList().trim(key, start, end);
            return null;
        }, "redis.impl.RedisServiceImpl.trim");

    }

    @Override
    public <T> long listRightPush(String key, T value) {
        return executeGetInHystrix(() -> redisTemplate.opsForList().rightPush(key, value),
                "redis.impl.RedisServiceImpl.listRightPush");
    }

    @Override
    public <T> T listLeftPop(String key) {
        return executeGetInHystrix(() -> (T) redisTemplate.opsForList().leftPop(key),
                "redis.impl.RedisServiceImpl.listLeftPop");
    }

    @Override
    public boolean exists(final String key) {
        return executeGetInHystrix(() -> (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        }), "redis.impl.RedisServiceImpl.exists");
    }

    @Override
    public Double opsForValueIncrement(String key, double value) {
        return executeGetInHystrix(() -> redisTemplate.opsForValue().increment(key, value),
                "redis.impl.RedisServiceImpl.opsForValueIncrement(java.lang.String, double)");
    }

    @Override
    public Long opsForValueIncrement(String key, long value) {
        return executeGetInHystrix(() -> redisTemplate.opsForValue().increment(key, value),
                "redis.impl.RedisServiceImpl.opsForValueIncrement(java.lang.String, long)");
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return executeGetInHystrix(() -> redisTemplate.executePipelined(action),
                "redis.impl.RedisServiceImpl.executePipelined");
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return redisTemplate.getKeySerializer();
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return redisTemplate.getHashKeySerializer();
    }

    @Override
    public <T> T getMapField(String key, String field) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    @Override
    public <K, V> ValueOperations<K, V> opsForValue() {
        return redisTemplate.opsForValue();
    }

    @Override
    public <T> void hPut(String key, T field, T value) {
        try {
            if (tryLock(key)) {
                executePutInHystrix(() -> {
                    redisTemplate.opsForHash().put(key, field, value);
                    return null;
                }, "redis.impl.RedisServiceImpl.hPut");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
        } finally {
            releaseLock(key);
        }
    }

    @Override
    public <T> void hPut(String key, String field, String value) {
        try {
            if (tryLock(key)) {
                stringRedisTemplate.opsForHash().put(key, field, value);
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
        } finally {
            releaseLock(key);
        }
    }

    @Override
    public Long hSize(String key) {
        return executeGetInHystrix(() -> redisTemplate.opsForHash().size(key),
                "redis.impl.RedisServiceImpl.hSize");
    }

    @Override
    public Long hIncr(String key, String field, long incredment) {
        return stringRedisTemplate.opsForHash().increment(key, field, incredment);
    }

    @Override
    public <HK, HV> HV hGet(String key, HK field) {
        return executeGetInHystrix(() -> {
            if (field != null) {
                HashOperations<String, HK, HV> hashOperations = redisTemplate.opsForHash();
                HV obj = hashOperations.get(key, field);
                if (obj != null) {
                    return obj;
                }
            }
            return null;
        }, "redis.impl.RedisServiceImpl.hGet");
    }

    @Override
    public <HK, HV> HV hGet(String key, String field) {
        if (field != null) {
            HashOperations<String, HK, HV> hashOperations = stringRedisTemplate.opsForHash();
            HV obj = hashOperations.get(key, field);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public <T> void hDel(String key, T field) {
        executePutInHystrix(() -> {
            redisTemplate.opsForHash().delete(key, field);
            return null;
        }, "redis.impl.RedisServiceImpl.hDel");
    }

    @Override
    public <HK, HV> Map<HK, HV> hgetAll(String key) {
        return executeGetInHystrix(() -> {
            HashOperations<String, HK, HV> hashOperations = redisTemplate.opsForHash();
            Map<HK, HV> obj = hashOperations.entries(key);
            if (obj != null) {
                return obj;
            }
            return null;
        }, "redis.impl.RedisServiceImpl.hgetAll");
    }

    @Override
    public <T> boolean hHasKey(String key, T field) {
        return executeGetInHystrix(() -> redisTemplate.opsForHash().hasKey(key, field),
                "redis.impl.RedisServiceImpl.hHasKey");
    }

    @Override
    public boolean setNX(String key, String value, long timeOutSeconds) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                    connection.close();
                    if (success) {
                        connection.expire(serializer.serialize(key), timeOutSeconds);
                    }
                    return success;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("setNX redis logInfoBusinessErrorService, key : {}", key);
        }
        return obj != null ? (Boolean) obj : false;
    }

    @Override
    public String getSet(String key, String value, long timeOutSeconds) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = connection.getSet(serializer.serialize(key), serializer.serialize(value));
                    if (ret != null) {
                        connection.expire(serializer.serialize(key), timeOutSeconds);
                    }
                    connection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception e) {
            LOGGER.error("setNX redis logInfoBusinessErrorService, key : {}", key);
        }
        return obj != null ? (String) obj : null;
    }

    @Override
    public <HK, HV> void hPutAll(String key, Map<HK, HV> map) {
        try {
            if (tryLock(key)) {
                executePutInHystrix(() -> {
                    redisTemplate.opsForHash().putAll(key, map);
                    return null;
                }, "redis.impl.RedisServiceImpl.hPutAll");
            } else {
                LOGGER.info("=============key:" + key + " missed the lock!");
            }
        } finally {
            releaseLock(key);
        }
    }

    /**
     * 执行get操作
     *
     * @param callable
     * @param methodName
     * @param <T>
     * @return
     */
    public <T> T executeGetInHystrix(Callable<T> callable, String methodName) {
        try {
            RedisHystrixCommand<T> tRedisHystrixCommand = new RedisHystrixCommand<>(callable, methodName);
            Object result = tRedisHystrixCommand.execute();
            if (result != null) {
                return (T) result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行put操作
     *
     * @param callable
     * @param methodName
     */
    public <T> T executePutInHystrix(Callable callable, String methodName) {
        RedisHystrixCommand<T> tRedisHystrixCommand = new RedisHystrixCommand<>(callable, methodName);
        Object result = tRedisHystrixCommand.execute();
        if (result != null) {
            return (T) result;
        }
        return null;
    }

    @Override
    public <HK, HV> List<HV> hMGet(String key, Collection<HK> fields) {
        List<HV> result = executeGetInHystrix(() -> {
            long start1 = Clock.systemUTC().millis();
            List<HV> list = redisTemplate.opsForHash().multiGet(key, fields);
            long end1 = Clock.systemUTC().millis();
            LOGGER.debug(Thread.currentThread().getName() + "线程，查到的集合size:" + list.size() + "================耗时:"
                    + (end1 - start1) + "毫秒");
            return list;
        }, "redis.impl.RedisServiceImpl.hMGet");
        if (result == null) {
            return Collections.EMPTY_LIST;
        }
        return result;
    }

    @Override
    public <T> List<T> range(String key, long start, long end) {
        return executeGetInHystrix(() -> redisTemplate.opsForList().range(key, start, end),
                "redis.impl.RedisServiceImpl.range");
    }

    @Override
    public boolean hasKey(String key) {
        return executeGetInHystrix(() -> {
            Boolean result = redisTemplate.hasKey(key);
            if (result) {
                return true;
            } else {
                return false;
            }
        }, "redis.impl.RedisServiceImpl.hasKey");
    }

    @Override
    public <T> void leftPushAll(String key, Collection<T> values) {
        executePutInHystrix(() -> {
            redisTemplate.opsForList().leftPushAll(key, values);
            return null;
        }, "redis.impl.RedisServiceImpl.leftPushAll");

    }

    @Override
    public <T> void rightPushAll(String key, Collection<T> values) {
        executePutInHystrix(() -> {
            redisTemplate.opsForList().rightPushAll(key, values);
            return null;
        }, "redis.impl.RedisServiceImpl.rightPushAll");
    }

    @Override
    public Object index(String key, long index) {
        return executeGetInHystrix(() -> redisTemplate.opsForList().index(key, index),
                "redis.impl.RedisServiceImpl.index");
    }

    @Override
    public RedisSerializer<?> getValueSerializer() {
        return redisTemplate.getValueSerializer();
    }

    @Override
    public <K, V> ListOperations<K, V> opsForList() {
        return redisTemplate.opsForList();
    }

    @Override
    public Set<String> hFieldsByKey(String key) {
        return executeGetInHystrix(() -> redisTemplate.opsForHash().keys(key),
                "redis.impl.RedisServiceImpl.index");
    }

    private String getJedisByKey(String key) {

        if (nodeMap == null || slotHostMap == null) {
            synchronized (this) {
                if (nodeMap == null || slotHostMap == null) {
                    nodeMap = jedisCluster.getClusterNodes();

                    String anyHost = nodeMap.keySet().iterator().next();

                    // getSlotHostMap方法在下面有
                    slotHostMap = getSlotHostMap(anyHost);
                }
            }
        }

        int slot = JedisClusterCRC16.getSlot(key);

        // 获取到对应的Jedis对象
        Map.Entry<Long, String> entry = slotHostMap.floorEntry(Long.valueOf(slot));

        if (entry == null) {
            System.out.println("key=" + key + ", slot=" + slot);
            return "";
        }
        return entry.getValue();
    }

    private static TreeMap<Long, String> getSlotHostMap(String anyHostAndPortStr) {
        TreeMap<Long, String> tree = new TreeMap<Long, String>();
        String[] parts = anyHostAndPortStr.split(":");
        HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
        try {
            Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            List<Object> list = jedis.clusterSlots();
            for (Object object : list) {
                List<Object> list1 = (List<Object>) object;
                List<Object> master = (List<Object>) list1.get(2);
                String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                tree.put((Long) list1.get(0), hostAndPort);
                tree.put((Long) list1.get(1), hostAndPort);
            }
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }

}
