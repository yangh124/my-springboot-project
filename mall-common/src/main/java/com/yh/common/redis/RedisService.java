package com.yh.common.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *  
 *
 * @title : Redis缓存服务
 * @description :
 * </pre>
 */
public interface RedisService {

    /**
     * 批量操作map
     *
     * @Description
     * @param key
     * @param map
     */
    <HK, T> void hPutAll(String key, Map<HK, T> map);

    /**
     * hash批量获取
     * 
     * @param key
     * @param fields
     * @return
     */
     <HK, HV> List<HV> hMGet(String key, Collection<HK> fields);

    /**
     * 获取key的类型
     *
     * @param key
     * @return
     */
    DataType getkeyType(String key);

    /**
     * 设置key的值成功则true，否则false
     * 
     * @Description
     * @param key
     * @param value
     * @return
     */

    <T> Boolean setIfAbsent(String key, T value);

    /**
     * 设置key的值成功则true，否则false
     * 
     * @Description
     * @param key
     * @param value
     * @param unixTime
     *            过期时间,单位毫秒
     * @return
     */
    <T> Boolean setIfAbsent(String key, T value, long unixTime);

    /**
     * 插入数据
     * 
     * @Description
     * @param key
     * @param value
     */
    <T> void set(String key, T value);

    <T> void set(byte[] key, byte[] value);

    /**
     * 插入数据
     * 
     * @Description
     * @param map
     * @param timeout
     */
    <T> void setPipelined(Map<String, T> map, Long timeout);
    
    /**
     * 插入数据（指定过期时间，单位秒）
     * 
     * @Description
     * @param key
     * @param value
     * @param timeout
     *            过期时间
     */
    <T> void set(String key, T value, Long timeout);

    /**
     * 获取指定 key值
     * 
     * @Description
     * @param key
     * @return
     */
    <T> T get(String key);

    byte[] getOne(String key);
    
    /**
     * 获取指定 key值
     * 
     * @Description
     * @return 批量获取到的列表
     */
    <T> List<T> getPipelined(List<String> keyList);

    /**
     * 删除指定key
     * 
     * @param key
     * @return
     */
    void del(String key);

    void sDel(String key);

    /**
     * 判断KEY是否存在
     * 
     * @Description
     * @param key
     * @return
     */
    boolean exists(final String key);

    /**
     * 设置指定key的失效时间<br/>
     * 失效
     * 
     * @Description
     * @param key
     * @param seconds
     *            单位秒
     * @return
     */
    boolean expireInSeconds(String key, int seconds);

    /**
     * 设置指定key的失效时间<br/>
     * 失效
     * 
     * @Description
     * @param key
     * @param milliSeconds
     *            毫秒
     * @return
     */
    boolean expireInMilliSeconds(String key, long milliSeconds);

    /**
     * 设置指定key在某时间失效<br/>
     * 失效后K/v不消失
     * 
     * @Description
     * @param key
     * @param unixTime 毫秒
     * @return 是否成功
     */
//    boolean expireAt(String key, long unixTime);

    /**
     * 获取指定key的存活时间
     * 
     * @param key
     * @return
     */
    Long getExpire(String key);

    <T>void addMap(T key, T field, T value, long time);

    /**
     * 通过正则匹配keys
     * 
     * @Description
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * list操作 -返回长度
     * 
     * @Description
     * @param key
     * @return
     */
    long listlength(String key);

    /**
     * list操作 -Left插入队
     * 
     * @Description
     * @param key
     * @param value
     * @return
     */
    <T> long listLeftPush(String key, T value);

    /**
     * 清理几条
     * @param key
     * @param start 删除的条数
     * @param end   删除的下标 （不包括下标）
     */
    void trim(String key, long start, long end);

    /**
     * list操作 -right插入队
     * 
     * @param key
     * @param value
     * @return
     */
    <T> long listRightPush(String key, T value);

    /**
     * list操作-出栈
     * 
     * @Description
     * @param key
     * @return
     */
    <T> T listLeftPop(String key);

    /**
     * 对某个KEY的值进行加法运算<br>
     * 1:只作用于值为数值类型，否则会出现异常
     * 
     * @param key
     * @param value
     * @return
     */
    Double opsForValueIncrement(String key, double value);

    /**
     * 进行自增操作
     * @param key
     * @param value
     * @return
     */
    Long opsForValueIncrement(String key, long value);

    /**
     * pipeline操作
     * 
     * @param action
     * @return
     */
    List<Object> executePipelined(final RedisCallback<?> action);

    /**
     * 获取key Serializer
     * 
     * @return
     */
    RedisSerializer<?> getKeySerializer();

    /**
     * 获取key Serializer
     * 
     * @return
     */
    RedisSerializer<?> getValueSerializer();

    /**
     * 获取hash key serializer
     * 
     * @return
     */
    RedisSerializer<?> getHashKeySerializer();

    /**
     * 获取map缓存中的某个对象
     *
     * @param key   map对应的key
     * @param field map中该对象的key
     * @return
     */
    <T> T getMapField(String key, String field);

    /**
     * 获取opsForValue
     * 
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> ValueOperations<K, V> opsForValue();

    /**
     * Hash put 值
     * 
     * @Description
     * @param key
     * @param field
     * @param value
     */
    <T> void hPut(String key, T field, T value);

    <T> void hPut(String key, String field, String value);

    /**
     * Hash size 值
     * @param key
     * @return
     */
    Long hSize(String key);

    Long hIncr(String key,String field,long value);

    /**
     * Hash get 值
     * 
     * @Description
     * @param key
     * @param field
     * @return 结果
     */
    <HK, HV> HV hGet(String key, HK field);

    <HK, HV> HV hGet(String key, String field);

    /**
     * Hash del 值
     * 
     * @Description
     * @param key
     * @param field
     */
    <T> void hDel(String key, T field);

    /**
     * Hash 指定 field是否存在
     * 
     * @Description
     * @param key
     * @param field
     * @return 是否存在
     */
    <T> boolean hHasKey(String key, T field);

    /**
     * 如果key不存在，则将key的值设为value; 否则不做操作
     * @param key
     * @param value
     * @param timeOutSeconds
     * @return
     */
    boolean setNX(final String key, final String value, long timeOutSeconds);

    /**
     * set成功后将值返回
     * @param key
     * @param value
     * @param timeOutSeconds
     * @return
     */
    String getSet(final String key, final String value, long timeOutSeconds);

    /**
     * Hash get 所有值
     * 
     * @Description
     * @param key
     * @return 获取到的结果
     */
    <HK, HV> Map<HK, HV> hgetAll(String key);

    /**
     * hash操作
     * @param <HK>
     * @param <HV>
     * @return
     */
    <HK, HV> HashOperations<String, HK, HV> opsForHash();

    /**
     * 左侧
     * @param key
     * @param values
     * @param <T>
     */
    <T> void leftPushAll(String key, Collection<T> values);

    /**
     * 右侧
     * @param key
     * @param values
     * @param <T>
     */
    <T> void rightPushAll(String key, Collection<T> values);

    /**
     * 范围获取
     * @param key
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    <T> List<T> range(String key, long start, long end);

    /**
     * 是否存在
     * @param key
     * @return
     */
    boolean hasKey(String key);

    /**
     * zset操作
     * @param <T>
     * @return
     */
    <T> ZSetOperations<String, T> opsForZSet();

    /**
     * 索引位置
     * @param key
     * @param index
     * @return
     */
    Object index(String key, long index);

    /**
     * list 操作
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> ListOperations<K, V> opsForList();

    /**
     * 获取key对应的属性
     * @param key
     * @return
     */
    Set<String> hFieldsByKey(String key);
}
