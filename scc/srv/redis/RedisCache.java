package scc.srv.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scc.utils.AzureProperties;

import java.time.Duration;

public class RedisCache {
	private static final String RedisHostname = AzureProperties.getProperty(AzureProperties.REDIS_URL);
	private static final String RedisKey = AzureProperties.getProperty(AzureProperties.REDIS_KEY);
	
	private static JedisPool instance;
	
	public synchronized static JedisPool getCachePool() {
		if( instance != null)
			return instance;
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		poolConfig.setMaxIdle(128);
		poolConfig.setMinIdle(16);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);
		//instance = new JedisPool(poolConfig, RedisHostname, 6380, 1000, RedisKey, true);
		instance = new JedisPool(poolConfig, "scc3phr-redis", 6379, 1000);
		return instance;

	}
}
