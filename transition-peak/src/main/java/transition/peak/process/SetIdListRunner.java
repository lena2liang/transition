package transition.peak.process;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class SetIdListRunner implements Runnable{

    BlockingQueue<String> idList = null;
    public SetIdListRunner(BlockingQueue<String> idList) {
        this.idList = idList;
    }
    public void run() {
        try {
            File file = new File("/data/transition/id.txt");
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(reader);
                String id = null;
                while ((id = bufferedReader.readLine()) != null) {
                    idList.put(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
