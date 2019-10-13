package edu.jlu.intell_home;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@SuppressLint("ValidFragment")
public class NewContentFragment extends Fragment{
    private LinearLayout layout;
    private LinearLayout buttonLayout;
    private  NewListFragment nlFragment;
    final List<String> list = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button button = null;
    private ArrayList<LinearLayout> layouts = new ArrayList<>();
    private Socket client = null;  //建立客户端
    private DataOutputStream dos;  //TCP通信数据输出流
    private DataInputStream dis;   //TCP通信数据输入流
    private String msg;
    private  int rec;
    private int  postion;
    private  int flag=0;
    @SuppressLint("ValidFragment")
    NewContentFragment(){}
    NewContentFragment(int postion) {
        this.postion=postion;
    }
    public Socket getClient() {
        return client;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        layout = view.findViewById(R.id.lineLayout);
        button = view.findViewById(R.id.button2);
        reThri(button);
        // 生成水平滚动条
        HorizontalScrollView scrollView = new HorizontalScrollView(getActivity());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        scrollView.setScrollBarSize(50);
        // 生成button的布局
        buttonLayout = new LinearLayout(getActivity());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));
        layout.addView(scrollView);
        scrollView.addView(buttonLayout);
        getArguments().getString("content");
        for(String s:getArguments().getString("content").split(",")) {
            list.add(s);
        }
        setButton(list);
        //getArgument获取传递过来的Bundle对象
        return view;
    }
    private void setButton(List<String> list) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final Button btn = new Button(getActivity());
                btn.setText(list.get(i));
                btn.setTextSize(30);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //第一种状态
                        if(flag==0) {
                            if (client == null || !client.isConnected()) {
                                Toast.makeText(getActivity(), "TCP连接不成功", Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ThreadCreClient();
                                    }
                                }).start();
                                SystemClock.sleep(300);
                            } else {
                                Toast.makeText(getActivity(), "TCP连接成功", Toast.LENGTH_SHORT).show();
                            }
                            btn.setBackgroundColor(Color.rgb(0,255,0));
                            if (null != dos) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        msg = "o";
                                        send(msg);
                                    }
                                }).start();
                                Toast.makeText(getActivity(), "Turn on", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "状态改变！但设备未连接", Toast.LENGTH_SHORT).show();
                            }
                            flag = 1;
                        } else {
                            //第二种状态
                            btn.setBackgroundColor(Color.rgb(255,255,255));
                            if (null != dos) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        msg = "f";
                                        send(msg);
                                    }
                                }).start();
                                Toast.makeText(getActivity(), "Turn off", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "状态改变！但设备未连接", Toast.LENGTH_SHORT).show();
                            }
                            flag = 0;
                        }

                    }
                });

                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
                buttons.add(btn);
                if (buttons.size() > layouts.size() * 5) {
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
                    layouts.add(layout);
                    buttonLayout.addView(layout);
                }
                layouts.get(layouts.size() - 1).addView(btn);
            }
        }
    }

    public void ThreadCreClient(){
        while(client == null||!client.isConnected()){
            try {
                client = new Socket("192.168.1.39",80);
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

    public void reThri(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(null!=client) {
                   try {
                       client.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
                Intent inte=new Intent();
                inte.setClass(getActivity(), FourthActivity.class);
                startActivity(inte);
                //SecondActivity.this.finish();
            }
        });
    }


}