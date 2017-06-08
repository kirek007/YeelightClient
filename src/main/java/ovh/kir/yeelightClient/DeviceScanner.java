package ovh.kir.yeelightClient;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class DeviceScanner {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private HashMap<String, DeviceInfo> devices;

    private ExecutorService executor = null;
    private final Scanner scanner = new Scanner();

    public void scanDevices() {
        if (scanner.isActive()) {
            log.info("Scanning is active.");
            return;
        }
        executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "DeviceScannerThread"));
        executor.execute(scanner);
    }

    public void stopScanning() {
        try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("Thread failed to shutdown. Killing it.");
        }
        executor.shutdownNow();
        int max_sleeps = 50;
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (--max_sleeps < 0 ) break;
        }
    }

}
