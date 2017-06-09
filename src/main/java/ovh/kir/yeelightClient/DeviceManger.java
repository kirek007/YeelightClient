package ovh.kir.yeelightClient;

import java.util.concurrent.ConcurrentHashMap;

class DeviceManger {

    ConcurrentHashMap<String, String> deviceList = new ConcurrentHashMap<>();

    public DeviceManger() {
    }
}
