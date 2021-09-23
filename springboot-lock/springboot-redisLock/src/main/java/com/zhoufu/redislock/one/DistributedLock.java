package com.zhoufu.redislock.one;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;

/**
 * @Author: zhoufu
 * @Date: 2021/3/4 11:11
 * @description:
 */
public class DistributedLock {
    private final JedisPool jedisPool;

    public DistributedLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 加锁
     * @param lockName   锁的key
     * @param acquireTimeout    获取超时时间
     * @param timeout   锁的超时时间
     * @return
     */
    public String lockWithTimeout(String lockName, long acquireTimeout, long timeout){
        Jedis conn = null;
        String retIdentifier = null;
        try {
            // 获取连接
            conn = jedisPool.getResource();
            // 随机生成一个 value
            String identifier = UUID.randomUUID().toString();
            // 锁名， 即key值
            String lockKey = "lock" + lockName;
            // 超时时间， 上锁后超过此时间则自动释放锁
            int lockExpire = (int) (timeout / 1000);
            // 获取锁的超时时间， 超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end){
                if (conn.setnx(lockKey, identifier) == 1){  // 存在
                    conn.expire(lockKey, lockExpire);
                    // 返回value值，用于释放锁时间确认
                    retIdentifier = identifier;
                    return retIdentifier;
                }
                // 返回 -1 代表key 没有设置超时时间，为key设置一个超时时间
                if (conn.ttl(lockKey) == -1){
                    conn.expire(lockKey, lockExpire);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }catch (JedisException e){
            e.printStackTrace();
        } finally {
            if (conn != null){
                conn.close();
            }
        }
        return null;
    }

    /**
     * 释放锁
     * @param lockName  锁的key
     * @param identifier 释放锁的标识
     * @return
     */
    public boolean releaseLock(String lockName, String identifier){
        Jedis conn = null;
        String lockKey = "lock:" + lockName;
        boolean retFlag = false;
        try {
            conn = jedisPool.getResource();
            while (true){
                // 监控lock， 准备开启事务
                conn.watch(lockKey);
                // 通过前面返回的value值判断是不是该锁，  是的话则删除，释放锁
                if (identifier.equals(conn.get(lockKey))){
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> exec = transaction.exec();
                    if (exec == null){
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e){
            e.printStackTrace();
        } finally {
            if (conn != null){
                conn.close();
            }
        }
        return retFlag;
    }
}
