package com.ichat.wannibu.ichat.Activity.MainInterface;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ichat.wannibu.ichat.ChatSQLite.ChatSQLiteHelper;
import com.ichat.wannibu.ichat.Adapter.ChatAdapter;
import com.ichat.wannibu.ichat.ChatSQLite.MyContentProvider;
import com.ichat.wannibu.ichat.ChatSQLite.PersonChat;
import com.ichat.wannibu.ichat.ChatServer.MySocket;
import com.ichat.wannibu.ichat.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {
    private ChatAdapter chatAdapter;
    private List<PersonChat> personChats = new ArrayList<PersonChat>();

    private TextView bar_chat_text;
    private Button bar_char_button;

    private ListView listView;

    private String NOTE;
    private String EMAIL;
    private String MYEMAIL;
    private Intent intent;

    private ChatSQLiteHelper helper;

    private Socket socket;
    public PrintWriter out;

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
        setContentView(R.layout.activity_chat);
        //弹出软键盘控制
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        socket = ((MySocket)getApplication()).getSocket();
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent = getIntent();
        NOTE = intent.getStringExtra("NOTE");
        EMAIL = intent.getStringExtra("EMAIL");
        MYEMAIL = intent.getStringExtra("MYEMAIL");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_chat);  //绑定自定义的布局
            bar_chat_text = (TextView)actionBar.getCustomView().findViewById(R.id.bar_chat_text);
            bar_chat_text.setText(NOTE);
            bar_char_button = (Button)actionBar.getCustomView().findViewById(R.id.bar_chat_button);
            bar_char_button.setOnClickListener(this);
        }

        listView = (ListView) findViewById(R.id.listview_chat);
        Button btn_chat_message_send = (Button) findViewById(R.id.btn_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);

        //设置监听MyContentProvider.CONTENT_URI对应的MyContentProvider提供的资源
        getContentResolver().registerContentObserver(MyContentProvider.CONTENT_URI,
                true,contentObserver);

        chatAdapter = new ChatAdapter(this, personChats);
        String t = MYEMAIL;
        t = t.replace("@","");
        t = t.replace(".","");
        t = t+".db";
        helper = new ChatSQLiteHelper(Chat.this,t);
        //获取本地聊天记录
        new Thread(new Runnable() {
            @Override
            public void run() {
                helper.query(personChats,MYEMAIL,EMAIL);
                handler.sendEmptyMessage(1);
            }
        }).start();
        listView.setAdapter(chatAdapter);

        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
                    Toast.makeText(Chat.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //输出到服务器
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        out.println(EMAIL+"&&&&"+et_chat_message.getText().toString());
                    }
                }).start();
                //输出到数据库
                ContentValues contentValues= new ContentValues();
                contentValues.put("id",MYEMAIL+"&&"+EMAIL);
                contentValues.put("message",et_chat_message.getText().toString());
                getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
                //清空输入框
                et_chat_message.setText("");
            }
        });
}

    @Override
    protected void onDestroy() {
        personChats.clear();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_chat_button:
                finish();
                break;
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    //监听是否有数据入库操作
    private ContentObserver contentObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            personChats.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    helper.query(personChats,MYEMAIL,EMAIL);
                    handler.sendEmptyMessage(1);
                }
            }).start();
            super.onChange(selfChange);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    chatAdapter.notifyDataSetChanged();
                    //ListView条目控制在最后一行
                    listView.setSelection(personChats.size());
                    break;
                default:
                    break;
            }
        };
    };

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
