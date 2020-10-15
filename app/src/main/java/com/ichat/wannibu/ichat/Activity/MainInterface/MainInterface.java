package com.ichat.wannibu.ichat.Activity.MainInterface;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ichat.wannibu.ichat.Activity.LogIn;
import com.ichat.wannibu.ichat.Adapter.FriendManage;
import com.ichat.wannibu.ichat.Adapter.MessageManage;
import com.ichat.wannibu.ichat.ChatSQLite.ChatSQLiteHelper;
import com.ichat.wannibu.ichat.Adapter.MyAdapter;
import com.ichat.wannibu.ichat.ChatSQLite.MyContentProvider;
import com.ichat.wannibu.ichat.ChatServer.MySocket;
import com.ichat.wannibu.ichat.R;
import com.ichat.wannibu.ichat.WebServer.Check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainInterface extends AppCompatActivity implements Runnable, View.OnClickListener{
    private long exitTime;
    private long RefreshTime;

    ImageButton button_message;
    ImageButton button_people;
    ImageButton button_me;
    TextView textView_message;
    TextView textView_people;
    TextView textView_me;

    ImageButton barBtn;
    TextView barText;
    Button btton2;
    Button btton3;
    Button bar_text;

    ListView listView;
    SimpleAdapter adapter;
    AdapterView.OnItemClickListener listener,listener1;

    Intent intent;
    private String UNAME;
    private String PWORD;
    private String ALLFRIENDS;
    private String[] eachfriends;
    private String[] friendnote;
    private String[] message;

    private PopupWindow popupwindow;

    private Socket socket;
    private BufferedReader br;
    private String receiveMsg;

    private ChatSQLiteHelper helper;

    private String t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //控制状态栏颜色
        final Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#f2f2f2"));
        //控制状态栏字体颜色
        changeStatusBarTextImgColor(false);
        changeStatusBarTextImgColor(true);

        setContentView(R.layout.activity_main_interface);

        //自定义actionabr
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_layout);  //绑定自定义的布局

            barText = (TextView)actionBar.getCustomView().findViewById(R.id.bar_text);
            barText.setText("联系人");
            barBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.bar_btn);
            barBtn.setOnClickListener(this);
        }

        intent = getIntent();
        UNAME = intent.getStringExtra("UNAME");
        PWORD = intent.getStringExtra("PWORD");
        ALLFRIENDS = intent.getStringExtra("ALLFRIENDS");

        ExitApplication.getInstance().addActivity(MainInterface.this);
        exitTime=System.currentTimeMillis();
        RefreshTime=System.currentTimeMillis();

        button_message = (ImageButton)findViewById(R.id.button_message);
        button_message.setOnClickListener(this);
        button_people = (ImageButton)findViewById(R.id.button_people);
        button_people.setOnClickListener(this);
        button_me = (ImageButton)findViewById(R.id.button_me);
        button_me.setOnClickListener(this);
        bar_text = (Button)findViewById(R.id.bar_text);
        bar_text.setOnClickListener(this);
        textView_message = (TextView)findViewById(R.id.textView_message);
        textView_people = (TextView)findViewById(R.id.textView_people);
        textView_me = (TextView)findViewById(R.id.textView_me);

        listView = (ListView)findViewById(R.id.listview_main);

        String[] temp;
        if(!"&".equals(ALLFRIENDS)){
            temp = ALLFRIENDS.split("&&");
            eachfriends = temp[0].split("&");
            friendnote = temp[1].split("&");
            FriendManage friendManage = new FriendManage(friendnote);
            List<Map<String,Object>> data = friendManage.getFriends();
            adapter = new SimpleAdapter(MainInterface.this,data,R.layout.friendslist,
                    new String[]{"image","friend"},new int[]{R.id.imageView,R.id.friendname});
            listView.setAdapter(adapter);
            message = new String[eachfriends.length];
        }

        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainInterface.this,EachFriend.class);
                intent.putExtra("EMAIL",eachfriends[position]);
                intent.putExtra("NOTE",friendnote[position]);
                intent.putExtra("MYEMAIL",UNAME);
                intent.putExtra("PWORD",PWORD);
                startActivity(intent);
            }
        };

        listener1 = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainInterface.this,EachFriend.class);
                intent.setClass(MainInterface.this,Chat.class);
                intent.putExtra("EMAIL",eachfriends[position]);
                intent.putExtra("NOTE",friendnote[position]);
                intent.putExtra("MYEMAIL",UNAME);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(listener);

        t = UNAME;
        t = t.replace("@","");
        t = t.replace(".","");
        t = t+".db";
        helper = ChatSQLiteHelper.getInstance(this,t);
        new Thread(this).start();//启动线程
    }

    @Override
    public void run() {
        socket = ((MySocket)getApplication()).getSocket();
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            ((MySocket)getApplication()).setSocket(socket);
            while(true){
                if ((receiveMsg = br.readLine()) != null){
                    handler.sendEmptyMessage(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收服务器来的消息
    String[] re = new String[2];
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response=(String)msg.obj;
            int what = msg.what;
            switch (what) {
                case 1:
                    re = receiveMsg.split("&&&&");
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("id",re[0]+"&&"+UNAME);
                    contentValues.put("message",re[1]);
                    int i;
                    int t = 1;
                    for(i=0; i<eachfriends.length; i++){
                        if(eachfriends[i].equals(re[0])){
                            message[i] = re[1];
                            t = 2;
                        }
                    }
                    i-=1;
                    if(t == 1){
                        message[i] = re[1];
                    }
                    Toast.makeText(MainInterface.this,"收到一条新信息",Toast.LENGTH_SHORT).show();
                    //震动 40ms
                    VibratorUtil.Vibrate(MainInterface.this.getBaseContext(),40);
                    button_message.performClick();//模拟按下,刷新消息
                    getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
                    break;
                case 2:
                    if(!response.equals("false")) {
                        ALLFRIENDS = response;
                        data();
                        String s = barText.getText().toString();
                        switch (s){
                            case "消息":
                                button_message.performClick();
                                break;
                            case "联系人":
                                button_people.performClick();
                                break;
                        }
                    }
                    break;
            }
        }
    };

    void data(){
        String[] temp;
        if(!"&".equals(ALLFRIENDS)){
            temp = ALLFRIENDS.split("&&");
            eachfriends = temp[0].split("&");
            friendnote = temp[1].split("&");
            message = new String[eachfriends.length];
        }
    }

    //控制震动
    public static class VibratorUtil {
        public static void Vibrate(final Context activity, long milliseconds) {
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        }
    }

    FriendManage friendManage;
    List<Map<String, Object>> list;
    Map<String, Object> map;
    MyAdapter myAdapter;

    MessageManage messageManage;
    List<Map<String,Object>> data1;

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_message:
                listView.setDividerHeight(60);
                labelSelected("消息");
                if(!"&".equals(ALLFRIENDS)){
                    messageManage = new MessageManage(friendnote,message);
                    data1 = messageManage.getMessage();
                    adapter = new SimpleAdapter(MainInterface.this,data1,R.layout.messagelist,
                            new String[]{"messagehead","messageuname","newmessage"},
                            new int[]{R.id.message_head,R.id.message_uname,R.id.newmessage});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(listener1);
                }
                else listView.setAdapter(null);
                break;
            case R.id.button_people:
                listView.setDividerHeight(60);
                labelSelected("联系人");
                if(!"&".equals(ALLFRIENDS)){
                    friendManage = new FriendManage(friendnote);
                    List<Map<String,Object>> data = friendManage.getFriends();
                    adapter = new SimpleAdapter(MainInterface.this,data,R.layout.friendslist,
                            new String[]{"image","friend"},new int[]{R.id.imageView,R.id.friendname});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(listener);
                }
                else listView.setAdapter(null);
                break;
            case R.id.button_me:
                listView.setDividerHeight(0);
                labelSelected("我");
                list = new ArrayList<Map<String, Object>>();
                map = new HashMap<String, Object>();
                map.put("head",R.drawable.user);
                map.put("email",UNAME);
                map.put("logout","退出登录");
                map.put("changep","修改密码");
                list.add(map);
                myAdapter = new MyAdapter(list,MainInterface.this);
                listView.setAdapter(myAdapter);
                break;
            case R.id.bar_btn:
                if (popupwindow != null&&popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }else{
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v,0,2);
                    popupwindow.setFocusable(true);
                }
                break;
            case R.id.button2:  //添加好友
                popupwindow.dismiss();
                intent.setClass(MainInterface.this,AddFriend.class);
                intent.putExtra("UNAME",UNAME);
                intent.putExtra("PWORD",PWORD);
                startActivity(intent);
                break;
            case R.id.button3:  //摇一摇
                popupwindow.dismiss();
                intent.setClass(MainInterface.this,ShakeIt.class);
                startActivity(intent);
                break;
            case R.id.bar_text:
                //5秒才可刷新一次
                if ((System.currentTimeMillis() - RefreshTime) < 5000) {
                    Toast.makeText(getApplicationContext(), "刷新不了", Toast.LENGTH_SHORT).show();
                    RefreshTime = System.currentTimeMillis();
                }else{
                    RefreshTime=System.currentTimeMillis();
                    final Check check = new Check();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String response = check.check("EMAIL",UNAME,"PASSW",PWORD,"Login");
                            Message message=new Message();
                            message.what=2;
                            message.obj=response;
                            handler.sendMessage(message);
                        }
                    }).start();
                    Toast.makeText(MainInterface.this,"刷新了",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //自定义右上角按钮弹出框
    public void initmPopupWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        popupwindow = new PopupWindow(customView, 240, 250);
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        btton2 = (Button) customView.findViewById(R.id.button2);
        btton3 = (Button) customView.findViewById(R.id.button3);
        btton2.setOnClickListener(MainInterface.this);
        btton3.setOnClickListener(MainInterface.this);
    }

    @Override
    public void onBackPressed() {
        if(popupwindow != null && popupwindow.isShowing()){
            popupwindow.dismiss();
            popupwindow = null;
        }else{
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else{
                ExitApplication.getInstance().exit();
            }
        }
    }

    void labelSelected(String label){
        switch (label){
            case "消息":
                barText.setText("消息");
                button_message.setBackgroundResource(R.mipmap.b1);
                textView_message.setTextColor(Color.parseColor("#35b435"));
                button_people.setBackgroundResource(R.mipmap.a2);
                textView_people.setTextColor(Color.parseColor("#000000"));
                button_me.setBackgroundResource(R.mipmap.c2);
                textView_me.setTextColor(Color.parseColor("#000000"));
                break;
            case "联系人":
                barText.setText("联系人");
                button_message.setBackgroundResource(R.mipmap.b2);
                textView_message.setTextColor(Color.parseColor("#000000"));
                button_people.setBackgroundResource(R.mipmap.a1);
                textView_people.setTextColor(Color.parseColor("#35b435"));
                button_me.setBackgroundResource(R.mipmap.c2);
                textView_me.setTextColor(Color.parseColor("#000000"));
                break;
            case "我":
                barText.setText("我");
                button_message.setBackgroundResource(R.mipmap.b2);
                textView_message.setTextColor(Color.parseColor("#000000"));
                button_people.setBackgroundResource(R.mipmap.a2);
                textView_people.setTextColor(Color.parseColor("#000000"));
                button_me.setBackgroundResource(R.mipmap.c1);
                textView_me.setTextColor(Color.parseColor("#35b435"));
                break;
        }
    }

    public void changeStatusBarTextImgColor(boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
