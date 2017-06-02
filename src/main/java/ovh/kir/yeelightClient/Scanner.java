package ovh.kir.yeelightClient;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scanner implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private static final String DISCOVERY_MSG =
            "M-SEARCH * HTTP/1.1\r\n" +
                    "HOST:239.255.255.250:1982\r\n" +
                    "MAN:\"ssdp:discover\"\r\n" +
                    "ST:wifi_bulb\r\n";
    private static final int MULTICAST_PORT = 1982;
    private static final String MULTICAST_ADDR = "239.255.255.250";
    private final int SOCKET_TIMEOUT = 2000;

    HashMap<String, DeviceInfo> deviceList = new HashMap<>();

    private AtomicBoolean scanning = new AtomicBoolean(true);

    @Override
    public void run() {
        scanning.set(true);
        log.info("Starting scanner");

        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Scanner stopped.");
        scanning.set(false);
    }

    private void process() throws IOException {
        byte[] searchPacket = DISCOVERY_MSG.getBytes();
        log.debug("Creating multicast socket.");
        MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        DatagramPacket packet;
        while(!Thread.currentThread().isInterrupted()) {
            byte[] buf = new byte[2048];
            packet = new DatagramPacket(buf, buf.length);
            try {
                log.debug("Sending discovery message.");
                socket.send(new DatagramPacket(searchPacket, searchPacket.length, InetAddress.getByName(MULTICAST_ADDR), MULTICAST_PORT));
                while(true) {
                    log.debug("Waiting for devices...");
                    socket.receive(packet);
                    String lightData = new String(packet.getData());
                    if (lightData.contains("HTTP/1.1 200 OK")) {
                        //Discovery response
                        DeviceInfo deviceInfo = DeviceInfoFactory.createFromDeviceResponse(lightData);
                        deviceList.put(deviceInfo.deviceId, deviceInfo);
                        log.info("Found device. Id: " + deviceInfo.deviceId);
                    } else if (lightData.contains("NOTIFY * HTTP/1.1")) {
                        //Notify message
                        DeviceInfo deviceInfo = DeviceInfoFactory.createFromDeviceResponse(lightData);
                        deviceList.put(deviceInfo.deviceId, deviceInfo);
                        log.info("Notification from device. Id: " + deviceInfo.deviceId);
                    } else {
                        log.info("Device respond with wrong header. Header value: " + lightData.split("\n")[0]);
                        continue;
                    }
                }
            } catch (SocketTimeoutException e) {
            }
        }
    }

    public HashMap<String, DeviceInfo> getFoundDevices() {
        HashMap<String, DeviceInfo> foundDevices = deviceList;
        deviceList.clear();
        return foundDevices;
    }

}
