package com.ichat.wannibu.ichat.Activity.MainInterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ichat.wannibu.ichat.R;

public class AddFriend extends AppCompatActivity {

    EditText email;
    Button find;

    private ProgressDialog progressDialog;
    Intent intent;

    String UNAME;
    String PWORD;

    com.ichat.wannibu.ichat.WebServer.AddFriend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //控制状态栏颜色
        final Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        //控制状态栏字体颜色
        changeStatusBarTextImgColor(false);
        changeStatusBarTextImgColor(true);

        setContentView(R.layout.activity_add_friend);

        intent = getIntent();
        UNAME = intent.getStringExtra("UNAME");
        PWORD = intent.getStringExtra("PWORD");

        email = (EditText)findViewById(R.id.editText_friendemail);
        find = (Button)findViewById(R.id.button_find);

        progressDialog = new ProgressDialog(AddFriend.this);

        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if("".equals(email.getText().toString())){
                    Toast.makeText(AddFriend.this,"输入邮箱查询好友",Toast.LENGTH_SHORT).show();
                }else{
                    if(email.getText().toString().equals(UNAME)){
                        Toast.makeText(AddFriend.this,"不能添加自己",Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.setMessage("处理中...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                friend = new com.ichat.wannibu.ichat.WebServer.AddFriend();
                                String response = friend.ckeckFriend(email.getText().toString());
                                Message message=new Message();
                                message.what=1;
                                message.obj=response;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String response = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    if(response.equals("false")){
                        Toast.makeText(AddFriend.this,"找不到该用户",Toast.LENGTH_SHORT).show();
                    }else{
                        addfriend();
                    }
                    break;
                case 2:
                    progressDialog.dismiss();
                    switch (response){
                        case  "false":
                            Toast.makeText(AddFriend.this,"错误",Toast.LENGTH_SHORT).show();
                            break;
                        case "existed":
                            Toast.makeText(AddFriend.this,"该用户已为您好友",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AddFriend.this,"添加成功",Toast.LENGTH_SHORT).show();
                            intent.setClass(AddFriend.this,MainInterface.class);
                            intent.putExtra("UNAME",UNAME);
                            intent.putExtra("PWORD",PWORD);
                            intent.putExtra("ALLFRIENDS",response);
                            startActivity(intent);
                            finish();
                    }
                    break;
            }
        }
    };

    public void addfriend(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);
        // 创建一个view，将布局加入view中
        View view = LayoutInflater.from(AddFriend.this).inflate(
                R.layout.addfriendnote, null, false);
        // 将view添加到builder中
        builder.setView(view);
        final Dialog dialog = builder.create();
        final EditText edt = (EditText) view.findViewById(R.id.editText_friendnote);
        Button confirm = (Button) view.findViewById(R.id.button_confirm);
        Button cancel = (Button) view.findViewById(R.id.button_cancel);
        confirm.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if("".equals(edt.getText().toString())){

                }else{
                    dialog.dismiss();
                    progressDialog.setMessage("处理中...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String response = friend.addFriend(UNAME,PWORD,email.getText().toString(),edt.getText().toString());
                            Message message=new Message();
                            message.what=2;
                            message.obj=response;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        });
        cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
