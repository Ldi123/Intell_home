package edu.jlu.intell_home;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
public class ThridActivity extends Activity implements OnClickListener {
    private EditText et_username;
    private EditText et_phonenum;
    private EditText et_imei;
    private EditText et_ip;
    private EditText et_room;
    private Button bt_post;
    private Button bt_test;
    private Button bt_inte;
    private String path;
    private String str1;
    private String ip;
    private String username;
    private String phonenum;
    private String imei;
    private String room_d;
    //    private HttpClient httpclient;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(ThridActivity.this, str1, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid);

        //my code below
        et_username = (EditText) findViewById(R.id.et_uesrname);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_imei=findViewById(R.id.et_imei);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_room = (EditText) findViewById(R.id.et_get);
        bt_post = (Button) findViewById(R.id.bt_post);
       bt_test=findViewById(R.id.button0);
       bt_inte=findViewById(R.id.bt_init);
       bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent();
                inte.setClass(ThridActivity.this, FourthActivity.class);
               startActivity(inte);
            }
          });
        // 启动按键监听器
      bt_post.setOnClickListener(this);
      bt_inte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" +et_ip.getText().toString().trim()+ ":8080//WebTest/Test.html");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        ip = et_ip.getText().toString().trim();
        username = et_username.getText().toString().trim();
        phonenum = et_phonenum.getText().toString().trim();
        imei = et_imei.getText().toString().trim();
        room_d = et_room.getText().toString().trim();
        switch (v.getId()) {
            case R.id.bt_post:
                //post路径不用添加username和password，这些内容写在包头里
                path = "http://" + ip + ":8080/WebTest/Web_link";
                //开启子线程
                new Thread() {
                    public void run() {
                        try {
                            str1 = readInputStreamByPost(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                            str1 = "POST网络超时";
                        }
                        handler.sendEmptyMessage(1);
                    }
                }.start();
                break;

        }

        //清理


    }

    /*
     实现Post方法提交数据
     */
    public String readInputStreamByPost(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("POST");
        String data = "username=" + URLEncoder.encode(username, "utf-8") + "&phonenum=" + URLEncoder.encode(phonenum, "utf-8")+"&imei=" + URLEncoder.encode(imei, "utf-8")+"&room_d=" + URLEncoder.encode(room_d, "utf-8");
        //post特殊定义参数
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", data.length() + "");
        conn.setDoOutput(true);
        OutputStream op = conn.getOutputStream();

        op.write(data.getBytes());
        //获得返回输入流
        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bop = new ByteArrayOutputStream();
        while ((len = is.read(buffer)) != -1) {
            bop.write(buffer, 0, len);
        }
        op.close();
        bop.close();
        is.close();
        String str = new String(bop.toByteArray(), "gbk"); //将服务器返回信息用gbk方式编码
        return str;
    }
}