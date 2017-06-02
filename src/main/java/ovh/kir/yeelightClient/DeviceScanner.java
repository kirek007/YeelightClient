package ovh.kir.yeelightClient;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class DeviceScanner {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private HashMap<String, DeviceInfo> devices;

    private ExecutorService executor = null;
    private final Scanner scanner = new Scanner();

    public void scanDevices() throws InterruptedException {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(scanner);
    }

    public void stopScanning() throws InterruptedException {
        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdownNow();
        while (!executor.isTerminated()) {

        }
    }

}
