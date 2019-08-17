package edu.jlu.intell_home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String TODO = null;
    File file;
    Button but1;
    Button but2;
    Context mcontext;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mcontext = getApplicationContext();
        tv1 = findViewById(R.id.text_imei);
        but1 = findViewById(R.id.button);
        but2 = findViewById(R.id.button1);
        JudgeIMEI();
        JudgeKey();
        showInformation();//获取IMEI，并显示到屏幕
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button) {

                    //复制IMEI码
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);// 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", tv1.getText());// 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(SecondActivity.this, "复制成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button1) {
                    //声明一个对话框对象
                    AlertDialog dialog;
                    //绑定当前界面窗口
                    dialog = new AlertDialog.Builder(SecondActivity.this).setTitle("提示")
                            .setMessage("请确保您已复制IMEI码并请认真填写定制内容")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent inte=new Intent();
//                                    inte.setData(Uri.parse("http://10.148.227.91"));
//                                    startActivity(inte);
                                    Intent inte = new Intent();
                                    inte.setClass(SecondActivity.this, ThridActivity.class);
                                    startActivity(inte);
                                }

                            })
                            .show();

                }
            }
        });
    }
    /**
     * 判断是否获得密钥
     */
    private void JudgeKey() {
        try {
            file = new File("/data/data/edu.jlu.intell_home/key.txt");
            if (file.exists() && file.length() != 0) {
             JudgeXML();
            } else {
                //声明一个对话框对象
                AlertDialog dialog;
                //绑定当前界面窗口
                dialog = new AlertDialog.Builder(this).setTitle("您还没有获得密钥")
                        .setMessage("请打开收到的密钥文件")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }

                        })
                        .setNegativeButton("稍后", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SecondActivity.this.finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * IO流判断是否已经完成了个性化定制
     */
    private void JudgeXML() {
        try {
            file = new File("/data/data/edu.jlu.intell_home/abc.xml");
            if (file.exists() &&file.length() != 0) {
                Intent inte=new Intent();
                inte.setClass(SecondActivity.this, FourthActivity.class);
                startActivity(inte);
                SecondActivity.this.finish();
            } else {
                //声明一个对话框对象
                AlertDialog dialog;
                //绑定当前界面窗口
                dialog = new AlertDialog.Builder(this).setTitle("你好像还没有定制个性化界面")
                        .setMessage("现在开始？")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("好的",  new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                //MainActivity.this.finish();
                                showInformation();
                            }

                        })
                        .setNegativeButton("稍后", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SecondActivity.this.finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * IO流判断是否已经完成了个性化定制
         */

    }
    private void JudgeIMEI() {
        InputStream in = null;
        try {
            file = new File("/data/data/edu.jlu.intell_home/Imei.txt");
            in = new BufferedInputStream(new FileInputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //声明一个对话框对象
                AlertDialog dialog;
                //绑定当前界面窗口
                dialog = new AlertDialog.Builder(this).setTitle("授权获取手机IMEI")
                        .setMessage("是否确认授权？")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                //MainActivity.this.finish();
                                showInformation();
                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SecondActivity.this.finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }
    public void showInformation() {
        //获取IMEI地址
        String imei = getIMEI(mcontext);
        tv1.setText(imei);
        try {
            FileOutputStream fi = new FileOutputStream("/data/data/edu.jlu.intell_home/Imei.txt");
            fi.write(imei.getBytes());
            fi.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        imei存储到SharedPreferences存储
//        SharedPreferences sp = getSharedPreferences("Imei", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("imei", imei);
//        editor.commit();
        //imei存储到文件
//        String state= Environment.getExternalStorageState();
//        if(state.equals(Environment.MEDIA_MOUNTED)){
//            File SDpath=Environment.getExternalStorageDirectory();
//            File file=new File(SDpath,"data.txt");
//            FileOutputStream fos;
//            try {
//                fos=new FileOutputStream(file);//把文件输出到data中
//                fos.write(("imei"+":"+imei).getBytes());//将我们写入的字符串变成字符数组）
//                System.out.println("write successfully!");
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return TODO;
        }
        String imei = telephonyManager.getDeviceId();
        return imei;
    }
}
