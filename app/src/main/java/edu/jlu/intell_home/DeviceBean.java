package edu.jlu.intell_home;
public class DeviceBean {
    private String Device_id;
    private String Device_name;
    private String IP;
    private String Port;
    private String Type;
    private String state;
    private String code;

    public void setDevice_id(String device_id) {
        Device_id = device_id;
    }
    public String getDevice_id() {
        return Device_id;
    }
    public void setDevice_name(String device_name) {
        Device_name = device_name;
    }
    public String getDevice_name() {
        return Device_name;
    }
    public void setIP(String IP) {
        this.IP = IP;
    }
    public String getIP() {
        return IP;
    }
    public void setPort(String port) {
        Port = port;
    }
    public String getPort() {
        return Port;
    }
    public void setType(String type) {
        Type = type;
    }
    public String getType() {
        return Type;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
