package transition.peak.process;

import redis.clients.jedis.Jedis;
import transition.redis.config.RedisClient;

import java.util.concurrent.Callable;

public class SingleGetCaller implements Callable<String> {

    Jedis client = null;
    String key = null;
    public SingleGetCaller(String key) {
        RedisClient redisClient = RedisClient.getInstance();
        client = redisClient.getClient();
        this.key = key;
    }
    public String call() {
        return client.hget("d_age", key);
    }
}
