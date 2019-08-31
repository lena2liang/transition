package transition.benchmark;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import transition.peak.process.IdListSizeLogRunner;
import transition.peak.process.RedisGetRunner;
import transition.peak.process.SetIdListRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransitionBenchmarkMain {
    static final MetricRegistry metrics = new MetricRegistry();
    static final BlockingQueue<String> idList = new ArrayBlockingQueue<String>(200000);
    static final Integer threadNum = 5;


    public static void main(String[] args) {

        Counter requests = metrics.counter("jobs");
        Timer timers = metrics.timer("times");

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        scheduledThreadPool.scheduleAtFixedRate(
                new IdListSizeLogRunner(idList), 0, 5, TimeUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.submit(new SetIdListRunner(idList));

        int threadNumGet = threadNum;
        if (args.length > 0) {
            threadNumGet = Integer.valueOf(args[0]);
        }
        for(int i = 0 ; i < threadNumGet ; i++) {
            executorService.submit(new RedisGetRunner(idList, requests, timers));
        }



    }
}
