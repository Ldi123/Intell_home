package edu.jlu.intell_home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mData;
    private List<RoomBean>  mList;
    private  Map<String, String> map = new HashMap<>();
    private List<Map<String, String>> listGet = new ArrayList<>();

    String host = "10.46.190.255";// 广播地址
    int port = 2333;// 广播的目的端口
    InetAddress adds ;
    DatagramSocket ds ;

    public void send_mes(String message){//传送要发送的指令

        try {

            adds = InetAddress.getByName(host);//通过ip，输出此对象的 IP 地址字符串和主机名
            ds = new DatagramSocket();//创建一个实例

            DatagramPacket dp = new DatagramPacket(message.getBytes(),message.length(), adds, port);
            //构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
            System.out.println("123");
            ds.send(dp);//发送信息
            System.out.println("456");
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("11111");
        }
        catch (SocketException e) {
            e.printStackTrace();
            System.out.println("2222");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("3333");
        }
    }

    public MyPagerAdapter(Context context ,List<String> list,List<RoomBean>  it_List) {
        mContext = context;
        mData = list;
        mList=it_List;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        for (DeviceBean studentGet : mList.get(position).getList()) {
            map.put("Room_id",mList.get(position).getRoom_id());
            map.put("Room_name", mList.get(position).getRoom_name());
            map.put("Device_id", studentGet.getDevice_id());
            map.put("Device_name", studentGet.getDevice_name());
            map.put("IP", studentGet.getIP());
            map.put("Port", studentGet.getPort());
            map.put("Type", studentGet.getType());
            map.put("state", studentGet.getState());
            map.put("code", studentGet.getCode());
            listGet.add(map);
            map = new HashMap<>();
        }
        String s0 =  listGet.get(position).get("Room_id");
        String s1 = listGet.get(position).get("Room_name");
        String s2=listGet.get(position).get("Device_id");
        String s3 = listGet.get(position).get("Device_name");
        String s4 = listGet.get(position).get("IP");
        String s5 = listGet.get(position).get("Port");
        String s6 = listGet.get(position).get("Type");
        String s7 = listGet.get(position).get("state");
        String s8 = listGet.get(position).get("code");
        View view = View.inflate(mContext, R.layout.page,null);
        TextView tv = view.findViewById(R.id.tv);
        final TextView text = view.findViewById(R.id.text);
        final Switch sw1=view.findViewById(R.id.switch1);
        sw1.setText(s3);
        sw1.setId(s2.charAt(0));
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //监听开关的改变
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
                if(is_checked){                 //可以理解为点击开启
                send_mes("Turn on1");
                }
                else{
                send_mes("Turn off1");
                }
            }
        });
        tv.setText(mData.get(position));
        text.append("Room_id:"+s0 + "\n");
        text.append("Room_name:"+s1 + "\n");
        text.append("Device_id:" + listGet.get(position).get("Device_id")+ "\n");
        text.append("Device_name" + listGet.get(position).get("Device_name") + "\n");
        text.append("IP:"+s4 + "\n");
        text.append("Port:"+s5 + "\n");
        text.append("Type:"+s6 + "\n");
        text.append("state:"+s7 + "\n");
        text.append("code:"+s8 + "\n");
        text.append("-----------------\n");
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position);
    }

}



