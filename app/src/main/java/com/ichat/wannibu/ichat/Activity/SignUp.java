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

import com.ichat.wannibu.ichat.WebServer.Check;
import com.ichat.wannibu.ichat.Legality.EmailCheck;
import com.ichat.wannibu.ichat.Legality.PasswCheck;
import com.ichat.wannibu.ichat.R;

public class SignUp extends AppCompatActivity {

    EditText emailA;    //邮箱
    EditText passW;     //密码
    EditText checkC;    //验证码
    Button btn_getCK;   //获取验证码
    Button btn_OK;      //确认注册
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

        setContentView(R.layout.activity_signup);
        btn_getCK = (Button)findViewById(R.id.button_getCK);
        btn_OK = (Button)findViewById(R.id.btn_signup);
        emailA = (EditText)findViewById(R.id.emailA);
        passW = (EditText)findViewById(R.id.passW);
        checkC = (EditText)findViewById(R.id.checkC);
        dialog = new ProgressDialog(SignUp.this);

        //实时监测邮箱输入框的内容
        emailA.addTextChangedListener(new TextWatcher() {
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
                    emailA.setText(str1);
                    emailA.setSelection(start);
                }
                //检测账号密码是否为空
                if(emailA.getText().toString().isEmpty()||passW.getText().toString().isEmpty()){
                    btn_getCK.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    btn_getCK.setEnabled(false);
                }
                else {
                    btn_getCK.setBackgroundColor(Color.parseColor("#32c732"));
                    btn_getCK.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //实时监测密码输入框的内容
        passW.addTextChangedListener(new TextWatcher() {
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
                    passW.setText(str1);
                    passW.setSelection(start);
                }
                //检测账号密码是否为空
                if(emailA.getText().toString().isEmpty()||passW.getText().toString().isEmpty()){
                    btn_getCK.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    btn_getCK.setEnabled(false);
                }
                else {
                    btn_getCK.setBackgroundColor(Color.parseColor("#32c732"));
                    btn_getCK.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_getCK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emailA.setEnabled(false);
                passW.setEnabled(false);
                EmailCheck emailCheck = new EmailCheck();
                if(emailCheck.emailCheck(emailA.getText().toString())){
                    PasswCheck passwCheck = new PasswCheck();
                    if(!passwCheck.passwCheck(passW.getText().toString())){
                        Toast.makeText(SignUp.this,"密码应为6-20的数字和英文组成",Toast.LENGTH_SHORT).show();
                        emailA.setEnabled(true);
                        passW.setEnabled(true);
                        passW.setText("");
                    }
                    else {
                        btn_OK.setBackgroundColor(Color.parseColor("#32c732"));
                        btn_OK.setEnabled(true);
                        btn_getCK.setEnabled(false);//获取验证码中，不允许点击获取验证码按钮
                        //获取验证码
                        new Thread(new Runnable() {
                            int i=30;   //获取验证码延时
                            @Override
                            public void run() {
                                for(; i>0; i--){
                                    btn_getCK.setText(String.valueOf(i) );
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                btn_getCK.setText("验证码");
                                Message message=new Message();
                                message.what=1;
                                handler.sendMessage(message); //传递可再次获取验证码消息
                            }
                        }).start();

                        dialog.setMessage("处理中...");
                        dialog.setCancelable(false);
                        dialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Check check = new Check();
                                String response = check.check("EMAIL",emailA.getText().toString(),"RequestCodes");
                                Message message=new Message();
                                message.what=2;
                                message.obj=response;
                                handler.sendMessage(message); //传递可验证码获取状态消息
                            }
                        }).start();
                    }
                }
                else{
                    Toast.makeText(SignUp.this,"邮箱格式有误",Toast.LENGTH_SHORT).show();
                    emailA.setEnabled(true);
                    passW.setEnabled(true);
                    passW.setText("");
                }
            }
        });

        //注册
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkC.getText().toString().isEmpty()){
                    dialog.setMessage("处理中...");
                    dialog.setCancelable(false);
                    dialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Check check = new Check();
                            String response = check.check("EMAIL",emailA.getText().toString(),"PASSW",passW.getText().toString(),
                                    "CODE",checkC.getText().toString(),"CCCode");
                            Message message=new Message();
                            message.what=3;
                            message.obj=response;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
                else{
                    Toast.makeText(SignUp.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String response=(String)msg.obj;
            switch (msg.what){
                case 1:
                    btn_getCK.setEnabled(true);
                    break;
                case 2:
                    dialog.dismiss();
                    switch(response){
                        case "existed":
                            Toast.makeText(SignUp.this, "此邮箱已注册", Toast.LENGTH_SHORT).show();
                            break;
                        case "true":
                            Toast.makeText(SignUp.this, "验证码已发送，请注意邮箱信息", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignUp.this, "未知错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 3:
                    dialog.dismiss();
                    if(response.equals("true")){
                        Toast.makeText(SignUp.this,"注册成功",Toast.LENGTH_SHORT).show();
                        intent.setClass(SignUp.this,LogIn.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(SignUp.this,"验证码有误",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        intent.setClass(SignUp.this,WLS.class);
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
