package edu.jlu.intell_home;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    private Socket client = null;  //建立客户端
    private DataOutputStream dos;  //TCP通信数据输出流
    private DataInputStream dis;   //TCP通信数据输入流
    private String msg;
    private  int rec;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadCreClient();
            }
        }).start();
        SystemClock.sleep(300);
        if(client==null||!client.isConnected()) {
            Toast.makeText(mContext,"TCP连接不成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext,"TCP连接成功",Toast.LENGTH_SHORT).show();
        }
        sw1.setText(s3);
        sw1.setId(s2.charAt(0));
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //监听开关的改变
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
                if(is_checked){

                    if(client==null||!client.isConnected()) {
                        Toast.makeText(mContext,"状态改变！但设备未连接",Toast.LENGTH_SHORT).show();
                        }else{
                        if(null!=dos) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    msg = "f";
                                    send(msg);
                                }
                            }).start();
                        }
                        Toast.makeText(mContext,"Turn on",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    if(client==null||!client.isConnected()) {
                        Toast.makeText(mContext,"状态改变！但设备未连接",Toast.LENGTH_SHORT).show();
                        }else{
                        if(null!=dos) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    msg = "o";
                                    send(msg);
                                }
                            }).start();
                        }
                        Toast.makeText(mContext,"Turn off",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        tv.setText(mData.get(position));
        text.append("第"+(position+1)+"个房间的定制信息\n");
        text.append("---------------------\n");
        text.append("Room_id:"+s0 + "\n");
        text.append("Room_name:"+s1 + "\n");
        text.append("Device_id:" + listGet.get(position).get("Device_id")+ "\n");
        text.append("Device_name:" + listGet.get(position).get("Device_name") + "\n");
        text.append("IP:"+s4 + "\n");
        text.append("Port:"+s5 + "\n");
        text.append("Type:"+s6 + "\n");
//        text.append("state:"+s7 + "\n");
//        text.append("code:"+s8 + "\n");
        text.append("---------------------\n");
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
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

    public void ThreadCreClient(){
        while(client == null||!client.isConnected()){
            try {
                client = new Socket("192.168.1.73",80);
                if(client.isConnected()) {//TCP连接成功
                    createIO();
                }
            } catch (IOException e) {
                e.printStackTrace();
                SystemClock.sleep(1000*2);
            }
        }
    }

    public void createIO(){

        try {
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            //relesas();//如果IO流建立失败用于释放资源
        }
    }

    public void send(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
            rec=dis.read();
            System.out.println(rec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
