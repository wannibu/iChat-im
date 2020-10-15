package com.ichat.wannibu.ichat.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ichat.wannibu.ichat.Activity.MainInterface.MainInterface;
import com.ichat.wannibu.ichat.ChatServer.MySocket;
import com.ichat.wannibu.ichat.WebServer.Check;
import com.ichat.wannibu.ichat.Legality.EmailCheck;
import com.ichat.wannibu.ichat.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LogIn extends AppCompatActivity {

    Button btn_login;   //登录
    Button btn_findpwd; //找回密码
    EditText editText_uname; //用户名
    EditText editText_pword; //密码
    private ProgressDialog dialog;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        final Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        //设置状态栏字体图标为白色
        changeStatusBarTextImgColor(false);
        //设置状态栏字体图标为黑色
        changeStatusBarTextImgColor(true);

        setContentView(R.layout.activity_login);
        dialog = new ProgressDialog(LogIn.this);
        editText_uname = (EditText) findViewById(R.id.uname);
        editText_pword = (EditText) findViewById(R.id.pword);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setEnabled(false);
                EmailCheck emailCheck = new EmailCheck();
                if(emailCheck.emailCheck(editText_uname.getText().toString())){
                    //登录操作
                    dialog.setMessage("登录中...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Check check = new Check();
                            String response = check.check("EMAIL",editText_uname.getText().toString(),"PASSW",
                                    editText_pword.getText().toString(), "Login");
                            Message message=new Message();
                            message.what=1;
                            message.obj=response;
                            handler.sendMessage(message);
                        }
                    }).start();
                }else {
                    Toast.makeText(LogIn.this,"邮箱格式有误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_findpwd = (Button)findViewById(R.id.button_findpwd);
        btn_findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogIn.this,"功能尚未开发",Toast.LENGTH_SHORT).show();
            }
        });

        //实时监测用户名输入框的内容
        editText_uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //清楚输入的空格
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editText_uname.setText(str1);
                    editText_uname.setSelection(start);
                }
                //检测账号密码是否为空
                if(editText_uname.getText().toString().isEmpty()||editText_pword.getText().toString().isEmpty()){
                    btn_login.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    btn_login.setEnabled(false);
                }
                else {
                    btn_login.setBackgroundColor(Color.parseColor("#32c732"));
                    btn_login.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //实时监测密码输入框的内容
        editText_pword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //清楚输入的空格
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editText_pword.setText(str1);
                    editText_pword.setSelection(start);
                }
                //检测账号密码是否为空
                if(editText_uname.getText().toString().isEmpty()||editText_pword.getText().toString().isEmpty()){
                    btn_login.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    btn_login.setEnabled(false);
                }
                else{
                    btn_login.setBackgroundColor(Color.parseColor("#32c732"));
                    btn_login.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String response=(String)msg.obj;
            switch (msg.what){
                case 1:
                    dialog.dismiss();
                    if(!response.equals("false")) {
                        Toast.makeText(LogIn.this, "登录成功", Toast.LENGTH_SHORT).show();
                        getSockst(editText_uname.getText().toString());//连接聊天服务器
                        //登录完成，进入主页面
                        intent.setClass(LogIn.this,MainInterface.class);
                        intent.putExtra("UNAME",editText_uname.getText().toString());
                        intent.putExtra("PWORD",editText_pword.getText().toString());
                        intent.putExtra("ALLFRIENDS",response);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        btn_login.setEnabled(true);
                        Toast.makeText(LogIn.this, "账户或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private Socket socket;
    public PrintWriter out;

    void getSockst(final String UNAME){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("106.54.72.253",8888);
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")), true);
                    ((MySocket)getApplication()).setSocket(socket);
                    out.println(UNAME);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        intent.setClass(LogIn.this,WLS.class);
        startActivity(intent);
        finish();
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
