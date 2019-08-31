package transition.redis.config;

import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

public class RedisClient {
    public static String host = "192.168.6.116";
    public static int port = 6379;
    private Jedis client;

    private static RedisClient instance = null;

    private RedisClient() {

    }

    public static RedisClient getInstance() {
        if (instance == null ) {
            return new RedisClient();
        }
        return instance;
    }


    public Jedis getClient() {
        return new Jedis(host, port);
    }

    public String get(String key) {
        return client.get(key);
    }
}
