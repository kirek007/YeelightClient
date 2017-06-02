package ovh.kir.yeelightClient;

import java.util.List;

public class DeviceInfo {
    String ip;
    int servicePort;
    String deviceId;
    String model;
    String name;
    String firmwareVersion;
    List<DeviceCommand> supportedActions;
    String power;
    DeviceColorMode colorMode;
    int bright;
    int colorTemperature;
    int rgb;
    int hue;
    int sat;

    public DeviceInfo(String ip, int servicePort, String deviceId, String model, String name, String firmwareVersion, List<DeviceCommand> supportedActions, String power, DeviceColorMode colorMode, int bright, int colorTemperature, int rgb, int hue, int sat) {
        this.ip = ip;
        this.servicePort = servicePort;
        this.deviceId = deviceId;
        this.model = model;
        this.name = name;
        this.firmwareVersion = firmwareVersion;
        this.supportedActions = supportedActions;
        this.power = power;
        this.colorMode = colorMode;
        this.bright = bright;
        this.colorTemperature = colorTemperature;
        this.rgb = rgb;
        this.hue = hue;
        this.sat = sat;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public List<DeviceCommand> getSupportedActions() {
        return supportedActions;
    }


}
