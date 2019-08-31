package transition.peak;

import redis.clients.jedis.Jedis;
import transition.redis.config.RedisClient;

public class TransitionPeakMain {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.getInstance();
        Jedis client = redisClient.getClient();
        String test = client.get("a");

    }
}
