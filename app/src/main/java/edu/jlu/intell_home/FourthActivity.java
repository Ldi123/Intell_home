package edu.jlu.intell_home;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.codec.binary.Base64;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class FourthActivity extends AppCompatActivity {
    public String web_hash;
    public static final String TAG = "FourthActivity";
    //TextView text;
    List<Map<String, String>> listGet = new ArrayList<>();
    Map<String, String> map = new HashMap<>();
    List<RoomBean> list = null;
    RoomBean bean = null;
    List<DeviceBean> sList = null;
    DeviceBean be = null;
    private ExpandableListView listView;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        Intent intent = getIntent();
        String action = intent.getAction();
        // listView =findViewById(R.id.list);
        // text = findViewById(R.id.textView);
    if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            String str = Uri.decode(uri.getEncodedPath());
            String res=str.substring(str.length()-3,str.length());
           if(res.equals("txt")) {
/**-----------------------------如果选择打开了txt文件就存储txt文件------------------------------**/
               try {
                   //找到目标文件
                   File file_out = new File("/data/data/edu.jlu.intell_home/key.txt");
                   if (!file_out.exists()) {
                       file_out.createNewFile();
                   }
                   if (file_out.length() == 0) {
                       //建立数据的输出通道
                       FileOutputStream fileOutputStream = new FileOutputStream(file_out);
                       BufferedReader br = new BufferedReader(new FileReader(str));//构造一个BufferedReader类来读取文件
                       String s = null;
                       while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                           fileOutputStream.write(s.getBytes());
                       }
                       br.close();
                       fileOutputStream.close();
                   }
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (Exception e) {
                   // TODO 自动生成的 catch 块
                   e.printStackTrace();
               }

    /**-----------------------------RSA解密对称密钥------------------------------**/
               try {
                   InputStream is= getAssets().open("key_pri.txt");
                   //建立缓冲数组配合循环读取文件的数据。
                   int length = 0; //保存每次读入缓冲区的字节总数
                   byte[] buf = new byte[1024]; //存储读取到的数据
                   StringBuilder StringBuilder = new StringBuilder();

                   while((length =is.read(buf))!=-1){ // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                       StringBuilder.append(new String(buf,0,length));
                   }
                   //关闭资源
                   is.close();
                   String privateKeyStr=StringBuilder.toString();


                   //找到目标文件
                   File  file_m = new File("/data/data/edu.jlu.intell_home/key.txt");
                   //建立数据的输入通道
                   FileInputStream fileInputStream_m = new FileInputStream(file_m);
                   //建立缓冲数组配合循环读取文件的数据。
                   int length1 = 0; //保存每次读入缓冲区的字节总数
                   byte[] buf1 = new byte[1024]; //存储读取到的数据
                   StringBuilder StringBuilder1 = new StringBuilder();

                   while((length1 = fileInputStream_m.read(buf1))!=-1){ // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                       StringBuilder1.append(new String(buf1,0,length1));
                   }
                   //关闭资源
                   fileInputStream_m.close();
                   String byte2Base64=StringBuilder1.toString();

                   //密文Base64解码
                   byte[] base642Byte =base642Byte(byte2Base64);
                   PrivateKey privateKey = string2PrivateKey(privateKeyStr);
                   byte[] privateDecrypt =privateDecrypt(base642Byte, privateKey);
//                解密后的明文
//                System.out.println("解密后的明文: " + new String(privateDecrypt));

                   //解密后写回
                   File file_out = new File("/data/data/edu.jlu.intell_home/key.txt");
                   //建立数据的输出通道
                   FileOutputStream fileOutputStream = new FileOutputStream(file_out);
                   fileOutputStream.write(privateDecrypt);
                   fileOutputStream.close();
               } catch (Exception e) {
                   // TODO 自动生成的 catch 块
                   e.printStackTrace();
               }
               Toast.makeText(this, "请退出APP然后打开收到的XML配置文件", Toast.LENGTH_SHORT).show();
           }else if(res.equals("xml")){

                /**-----------------------------如果选择打开了XML文件就解密XML文件------------------------------**/
                try {

                    //找到密钥文件
                    File file_key = new File("/data/data/edu.jlu.intell_home/key.txt");
                    //建立数据的输入通道
                    FileInputStream fileInputStream_key = new FileInputStream(file_key);
                    //建立缓冲数组配合循环读取文件的数据。
                    int length_key = 0; //保存每次读入缓冲区的字节总数
                    byte[] buf_key = new byte[1024]; //存储读取到的数据
                    StringBuilder StringBuilder_key = new StringBuilder();
                    while ((length_key = fileInputStream_key.read(buf_key)) != -1) { // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                        StringBuilder_key.append(new String(buf_key, 0, length_key));
                    }
                    //关闭资源
                    fileInputStream_key.close();
                    String key = StringBuilder_key.toString();

                    //找到密文
                    File file_in = new File(str);
                    //建立数据的输入通道
                    FileInputStream fileInputStream = new FileInputStream(file_in);
                    //建立缓冲数组配合循环读取文件的数据。
                    int length = 0; //保存每次读入缓冲区的字节总数
                    byte[] buf = new byte[1024]; //存储读取到的数据
                    StringBuilder StringBuilder = new StringBuilder();

                    while ((length = fileInputStream.read(buf)) != -1) { // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                        StringBuilder.append(new String(buf, 0, length));
                    }
                    //关闭资源
                    fileInputStream.close();
                    //解密
                    String decrypt = aesDecrypt(StringBuilder.toString(), key);

                    //解密后写回
                    File file_out = new File(str);
                    //建立数据的输出通道
                    FileOutputStream fileOutputStream = new FileOutputStream(file_out);
                    fileOutputStream.write(decrypt.getBytes());
                    fileOutputStream.close();
                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }

/**-----------------------------解密后的XML文件写入本地副本，方便后续实现持久化------------------------------**/
               try {
                   FileOutputStream fi = new FileOutputStream("/data/data/edu.jlu.intell_home/abc.xml");
                   StringBuilder result = new StringBuilder();
                   BufferedReader br = new BufferedReader(new FileReader(str));//构造一个BufferedReader类来读取文件
                   String s = null;
                   while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                       result.append(System.lineSeparator() + s);
                       fi.write(s.getBytes());
                   }
                   br.close();
                   fi.close();
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }


            try {
                /**-----------------------------解析XML文件，HASH验证-------------------------------**/
                int i = 0;
                File file = new File("/data/data/edu.jlu.intell_home/abc.xml");
                // 读取文件-->以流的方式
                FileInputStream fileInputStream = new FileInputStream(file);
                final List<RoomBean> Rooms = parseFile(fileInputStream);
                //获得解析到的结果
                while ((i < Rooms.size())) {
                    for (DeviceBean studentGet : Rooms.get(i).getList()) {
                        map.put("Room_id", Rooms.get(i).getRoom_id());
                        map.put("Room_name", Rooms.get(i).getRoom_name());
                        map.put("Device_id", studentGet.getDevice_id());
                        map.put("Device_name", studentGet.getDevice_name());
                        map.put("IP", studentGet.getIP());
                        map.put("Port", studentGet.getPort());
                        map.put("Type", studentGet.getType());
                        map.put("state", studentGet.getState());
                        map.put("code", studentGet.getCode());
                        listGet.add(map);
                        i += 1;
                        map = new HashMap<>();
                    }
                }

                /**-----------------------------HASH加密IMEI码-----------------------------------**/
                //找到目标文件
                File fileIMEI = new File("/data/data/edu.jlu.intell_home/Imei.txt");
                //建立数据的输入通道
                FileInputStream fileInputStreamIMEI = new FileInputStream(fileIMEI);
                //建立缓冲数组配合循环读取文件的数据。
                int length = 0; //保存每次读入缓冲区的字节总数
                byte[] buf = new byte[1024]; //存储读取到的数据
                StringBuilder StringBuilder = new StringBuilder();
                while ((length = fileInputStreamIMEI.read(buf)) != -1) { // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                    StringBuilder.append(new String(buf, 0, length));
                }
                //关闭资源
                fileInputStreamIMEI.close();
                String ss = StringBuilder.toString();
                String sss = getMD5(ss);
                StringBuilder strB = new StringBuilder();
                strB.append(sss.charAt(11));
                strB.append(sss.charAt(30));
                strB.append(sss.charAt(23));
                setVp(Rooms,strB.toString());
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
               //其他文件类型暂不处理，所以直接加载已有界面
        try {
            /**-----------------------------解析XML文件，HASH验证-------------------------------**/
            int i = 0;
            File file = new File("/data/data/edu.jlu.intell_home/abc.xml");
            // 读取文件-->以流的方式
            FileInputStream fileInputStream = new FileInputStream(file);
            final List<RoomBean> Rooms = parseFile(fileInputStream);
            //获得解析到的结果
            while ((i < Rooms.size())) {
                for (DeviceBean studentGet : Rooms.get(i).getList()) {
                    map.put("Room_id", Rooms.get(i).getRoom_id());
                    map.put("Room_name", Rooms.get(i).getRoom_name());
                    map.put("Device_id", studentGet.getDevice_id());
                    map.put("Device_name", studentGet.getDevice_name());
                    map.put("IP", studentGet.getIP());
                    map.put("Port", studentGet.getPort());
                    map.put("Type", studentGet.getType());
                    map.put("state", studentGet.getState());
                    map.put("code", studentGet.getCode());
                    listGet.add(map);
                    i += 1;
                    map = new HashMap<>();
                }
            }

            /**-----------------------------HASH加密IMEI码-----------------------------------**/
            //找到目标文件
            File fileIMEI = new File("/data/data/edu.jlu.intell_home/Imei.txt");
            //建立数据的输入通道
            FileInputStream fileInputStreamIMEI = new FileInputStream(fileIMEI);
            //建立缓冲数组配合循环读取文件的数据。
            int length = 0; //保存每次读入缓冲区的字节总数
            byte[] buf = new byte[1024]; //存储读取到的数据
            StringBuilder StringBuilder = new StringBuilder();
            while ((length = fileInputStreamIMEI.read(buf)) != -1) { // read方法如果已经到达文件末尾而没有更多的数据，则返回 -1。
                StringBuilder.append(new String(buf, 0, length));
            }
            //关闭资源
            fileInputStreamIMEI.close();
            String ss = StringBuilder.toString();
            String sss = getMD5(ss);
            StringBuilder strB = new StringBuilder();
            strB.append(sss.charAt(11));
            strB.append(sss.charAt(30));
            strB.append(sss.charAt(23));
            setVp(Rooms,strB.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }


// Log.i(TAG, person.toString());
//                for (int j = 0; j < listGet.size(); j++) {
//                    String s1 = listGet.get(j).get("Room_name");
//                    generateBtnList(s1,j);
//                }



    private void setVp (List < RoomBean > Rooms,String app_hash) {
        /**------HASH验证------**/
        if (app_hash.equals(web_hash)) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < Rooms.size(); i++) {
                list.add(Rooms.get(i).getRoom_name());
            }

            ViewPager vp = (ViewPager) findViewById(R.id.vp);
            vp.setAdapter(new MyPagerAdapter(this, list, Rooms));
        } else {
            Toast.makeText(this, "验证时检测到IMEI已改变", Toast.LENGTH_SHORT).show();
            //声明一个对话框对象
            AlertDialog dialog;
            //绑定当前界面窗口
            dialog = new AlertDialog.Builder(FourthActivity.this).setTitle("IMEI验证错误！")
                    .setMessage("这可能不是您的手机...")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FourthActivity.this.finish();
                        }
                    }).create();
            dialog.show();
        }
    }
//
//    private void getList(int j){
//        String s0=  listGet.get(j).get("Room_id");
//        String s1 = listGet.get(j).get("Room_name");
//        String s2 = listGet.get(j).get("Device_id");
//        String s3 = listGet.get(j).get("Device_name");
//        String s4 = listGet.get(j).get("IP");
//        String s5 = listGet.get(j).get("Port");
//        String s6 = listGet.get(j).get("Type");
//        String s7 = listGet.get(j).get("state");
//        String s8 = listGet.get(j).get("code");
//        generateBtnList(s3,j);
//        text.append("Room_id:"+s0 + "\n");
//        text.append("Room_name:"+s1 + "\n");
//        text.append("Device_id:" + s2 + "\n");
//        text.append("Device_name" + s3 + "\n");
//        text.append("IP:"+s4 + "\n");
//        text.append("Port:"+s5 + "\n");
//        text.append("Type:"+s6 + "\n");
//        text.append("state:"+s7 + "\n");
//        text.append("code:"+s8 + "\n");
//        text.append("-----------------\n");
//        text.append("num:"+j + "\n");
//    }

    private List<RoomBean> parseFile (InputStream is){
        try {
            // 获取文件解析类
            XmlPullParser parser = Xml.newPullParser();
            // 设置解析类的数据和编码格式
            parser.setInput(is, "utf-8");
            // 根据事件获取节点
            int event = parser.getEventType();
            // 判断文档是否读取完毕
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        // 文档开始--创建集合
                        list = new ArrayList<RoomBean>();
                        break;
                    case XmlPullParser.START_TAG:  // 标签开始
                        //
                        if ("Hash".equals(parser.getName())) {    //解析Hash
                            web_hash=parser.nextText();
                            //    text.append("Hash:"+parser.nextText()+"\n");
                        } else if ("Room".equals(parser.getName())) {    //解析Room
                            // 创建RoomBean
                            bean = new RoomBean();
                            // 属性节点根据索引获取
                            bean.setRoom_id(parser.getAttributeValue(0));
                            bean.setRoom_name(parser.getAttributeValue(1));
                            sList = new ArrayList<DeviceBean>();
                        } else if ("Device".equals(parser.getName())) {    //解析Device
                            // 创建DeviceBean
                            be = new DeviceBean();
                            // 属性节点根据索引获取
                            be.setDevice_id(parser.getAttributeValue(0));
                            sList.add(be);
                        } else if ("name".equals(parser.getName())) {      //解析Device节点下各节点
                            be.setDevice_name(parser.nextText());
                        } else if ("IP".equals(parser.getName())) {
                            be.setIP(parser.nextText());
                        } else if ("Port".equals(parser.getName())) {
                            be.setPort(parser.nextText());
                        } else if ("Type".equals(parser.getName())) {
                            be.setType(parser.nextText());
                        } else if ("state".equals(parser.getName())) {
                            be.setState(parser.nextText());
                        } else if ("code".equals(parser.getName())) {
                            be.setCode(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 标签结束
                        if ("Room".equals(parser.getName())) {
                            // sList存进bean类
                            bean.setList(sList);
                            // bean类存进集合
                            list.add(bean);
                            bean = null;
                        }
                        break;
                }
                // 获取下个标签内数据
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**--------RSA私钥解密--------**/
    public static PrivateKey string2PrivateKey(String priStr) throws Exception{
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = cipher.doFinal(content);

        return bytes;

    }

    //Base64编码转字节数组

    public static byte[] base642Byte(String base64Key) throws IOException{
        return Base64.decodeBase64(base64Key.getBytes());

    }

    /**----------------------MD5验证---------------------**/
    public String getMD5 (String ss) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");//创建具有指定算法名称的摘要
            md.update(ss.getBytes());                    //使用指定的字节数组更新摘要
            byte mdBytes[] = md.digest();                 //进行哈希计算并返回一个字节数组
            String hash = "";
            for (int i = 0; i < mdBytes.length; i++) {           //循环字节数组
                int temp;
                if (mdBytes[i] < 0)                          //如果有小于0的字节,则转换为正数
                    temp = 256 + mdBytes[i];
                else
                    temp = mdBytes[i];
                if (temp < 16)
                    hash += "0";
                hash += Integer.toString(temp, 16);         //将字节转换为16进制后，转换为字符串
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }





    /**

     * 解密

     * @param strKey

     * @param content

     * @return

     * @throws Exception

     */

    public static String decrypt(byte[] content,String strKey ) throws Exception {

        SecretKeySpec skeySpec = getKey(strKey);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(content);

        String originalString = new String(original);

        return originalString;

    }



    private static SecretKeySpec getKey(String strKey) throws Exception {

        byte[] arrBTmp = strKey.getBytes();

        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）



        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {

            arrB[i] = arrBTmp[i];

        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;

    }

    /**

     * base 64 encode

     * @param bytes 待编码的byte[]

     * @return 编码后的base 64 code

     */
    /**

     * base 64 decode

     * @param base64Code 待解码的base 64 code

     * @return 解码后的byte[]

     * @throws Exception

     */

    public static byte[] base64Decode(String base64Code) throws Exception{

        return base64Code.isEmpty() ? null : Base64.decodeBase64(base64Code.getBytes());
    }

    /**

     * 将密文base 64解码后再解密

     * @param encryptStr 密文

     * @param decryptKey 解密密钥

     * @return 解密后的string

     * @throws Exception

     */

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {

        return encryptStr.isEmpty() ? null : decrypt(base64Decode(encryptStr), decryptKey);

    }
//    private void generateBtnList(String text,int index) {
//        LinearLayout mBtnListLayout;
//        mBtnListLayout = findViewById(R.id.btnListLayout);
//        Button codeBtn = new Button(this);
//        setBtnAttribute(mBtnListLayout, codeBtn, text, index, Color.rgb(255, 128, 0), Color.BLACK, 18);
//        mBtnListLayout.addView(codeBtn);
//    }
//
//    private void setBtnAttribute(LinearLayout mBtnListLayout, final Button codeBtn, String text,  int id, int backGroundColor, int textColor, int textSize) {
//        if (null == codeBtn) {
//            return;
//        }
//        codeBtn.setBackgroundColor(backGroundColor);
//        codeBtn.setTextColor((textColor >= 0) ? textColor : Color.BLUE);
//        codeBtn.setTextSize(textSize);
//        codeBtn.setId(id);
//        codeBtn.setText(text);
//        codeBtn.setGravity(Gravity.CENTER);
//        codeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                intent.setData(Uri.parse("http://10.46.190.179:8080/TestWebA/NewFile.jsp"));
//                startActivity(intent);
//                Intent inte=new Intent();
//                inte.setClass(ThridActivity.this, FourthActivity.class);
//                startActivity(inte);
//                if(v.getId()==codeBtn.getId()){
//                    getList(codeBtn.getId());
//                }
//            }
//        });
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_START);
//        rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        codeBtn.setLayoutParams(rlp);
//        id++;
//    }

}