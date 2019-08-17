package edu.jlu.intell_home;
import java.util.List;
public class RoomBean {
    private String Room_id;
    private String Room_name;
    private List<DeviceBean> list;

    public void setRoom_id(String room_id) {
        Room_id = room_id;
    }

    public String getRoom_id() {
        return Room_id;
    }

    public void setRoom_name(String room_name) {
        Room_name = room_name;
    }

    public String getRoom_name() {
        return Room_name;
    }

    public void setList(List<DeviceBean> list) {
        this.list = list;
    }

    public List<DeviceBean> getList() {
        return list;
    }
}