package transition.peak.process;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class IdListSizeLogRunner implements Runnable {

    BlockingQueue<String> idList = null;
    public IdListSizeLogRunner(BlockingQueue<String> idList) {
        this.idList = idList;

    }

    public void run() {
        log.info("IdQueueListSize {}", idList.size());
    }
}
