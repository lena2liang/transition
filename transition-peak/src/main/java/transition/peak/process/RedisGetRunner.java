package transition.peak.process;

import com.codahale.metrics.Counter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import com.codahale.metrics.Timer;
import transition.redis.config.RedisClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisGetRunner implements Runnable {

    Jedis client = null;
    BlockingQueue<String> idList = null;
    Counter counter = null;
    Timer timer = null;

    public RedisGetRunner(BlockingQueue<String> idList, Counter requests, Timer timer) {
        RedisClient redisClient = RedisClient.getInstance();
        client = redisClient.getClient();
        this.idList = idList;
        this.counter = requests;
        this.timer = timer;

    }
    public void run() {

        try {
            while (true) {
                String id = idList.poll();
                if (id == null) {
                    Thread.sleep(200);
                }
                if (id != null) {
                    Timer.Context context = timer.time();

                    try {


//                        Future<String> task = new FutureTask<String>(new SingleGetCaller(id));
//                        new Thread((Runnable) task).start();

                        String age1 = client.hget("d_age", id);
//                        String age2 = task.get();
                    } finally {
                        context.stop();
                    }
                    counter.inc();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
