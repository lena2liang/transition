package com.transition.generator;

import com.transition.generator.utils.ShortMd5;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Generator {

    protected int incremental;
    private Map<Integer, FileWriter> writerMap = new HashMap<>();
    private int splits = 997;

    public Generator(int incremental) {
        this.incremental = incremental;
    }

    String toMd5(Long number) {
//        return ShortMd5.shortMd5(DigestUtils.md5Hex(String.valueOf(number)));
        return ShortMd5.shortMd5(DigestUtils.md5Hex(String.valueOf(number)));
    }

    protected abstract Queue<Long> input();

    protected void map(int threadNum, final Queue<Long> queue) {
        final CountDownLatch latch = new CountDownLatch(threadNum);
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        for (int i=0;i<threadNum;i++) {
            service.submit(() -> {
                try {
                    while (queue.size() != 0) {
                        Long item = queue.poll();

                        for (int j=0;j<incremental;j++){
                            Long ret = item + j;
                            String md5 = toMd5(ret);
                            int fileNum = md5.hashCode() % splits;
                            if(fileNum < 0) {
                                fileNum = fileNum * -1;
                            }
                            if (!writerMap.containsKey(fileNum)) {
                                writerMap.put(fileNum, new FileWriter(String.format("./%s.tmp", fileNum), true));
                            }
                            FileWriter writer = writerMap.get(fileNum);
                            writer.write(md5 + "\t" + String.valueOf(ret) + '\n');
                        }
                    }

                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            latch.await();
            for (FileWriter writer : writerMap.values()) {
                writer.flush();
                writer.close();
            }
        service.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void reduce(int threadNum) {
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(splits);
        for (int i=0;i<splits;i++) {
            service.submit(new ReadRunnable(i, countDownLatch));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    protected static void run(String[] args, Generator generator) {
        int threadNum = 1;
        if (args.length > 0) {
            threadNum = Integer.parseInt(args[0]);
        }
        int reduceThread = 1;
        if (args.length > 1) {
            reduceThread = Integer.parseInt(args[1]);
        }
        Queue<Long> queue = generator.input();
        generator.map(threadNum, queue);
        generator.reduce(reduceThread);

    }

    public static String join(List<?> list, String delim) {
        int len = list.size();
        if (len == 0)
            return "";
        StringBuilder sb = new StringBuilder(list.get(0).toString());
        for (int i = 1; i < len; i++) {
            sb.append(delim);
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }

    class ReadRunnable implements Runnable {

        private int i;
        private CountDownLatch latch;
        public ReadRunnable(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(String.format("File starting to merge: %s.tmp", i));
                Map<String, Set<String>> setMap = new HashMap<>();
                BufferedReader reader = new BufferedReader(new FileReader(new File("").getAbsolutePath() + String.format("/%s.tmp", i)));

                LineIterator iterator = FileUtils.lineIterator(new File(new File("").getAbsolutePath() + String.format("/%s.tmp", i)));
                int line = 0;
                while (iterator.hasNext()){
                    String[] ret = StringUtils.splitPreserveAllTokens(iterator.next(), "\t");

                    if (ret.length != 2) {
                        continue;
                    }

                    Set<String> valueSet = setMap.get(ret[0]);
                    if (valueSet == null) {
                        valueSet = new HashSet<>();
                        setMap.put(ret[0], valueSet);
                    }
                    valueSet.add(ret[1]);

                    if (line % 1000000 == 0) {
                        System.out.println(String.format("Read total line: %s", line));
                    }
                    line++;
                }
                iterator.close();

                FileWriter writer = new FileWriter(String.format("./%s.txt", i), true);
                for(Map.Entry<String, Set<String>> entry : setMap.entrySet()) {
                    String value = join(new ArrayList<>(entry.getValue()), ",");
                    writer.write(entry.getKey() + '\t' + value);
                    writer.write('\n');
                }
                writer.close();
                setMap.clear();
                System.out.println(String.format("File finished merge: %s.tmp", i));
            } catch (Exception e) {
                e.printStackTrace();
            }

            latch.countDown();

        }
    }

}
