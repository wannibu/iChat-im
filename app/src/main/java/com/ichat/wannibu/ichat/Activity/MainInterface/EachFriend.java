package com.ichat.wannibu.ichat.Activity.MainInterface;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ichat.wannibu.ichat.ChatSQLite.ChatSQLiteHelper;
import com.ichat.wannibu.ichat.R;
import com.ichat.wannibu.ichat.WebServer.ChangeNote;
import com.ichat.wannibu.ichat.WebServer.DeleteFriend;

public class EachFriend extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    private String EMAIL;
    private String NOTE;
    private String MYEMAIL;
    private String PWORD;

    TextView note;
    TextView email;
    Button send;
    Button delete;
    Button button_changenote;
    private ProgressDialog progressDialog;

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

        setContentView(R.layout.activity_each_friend);

        progressDialog = new ProgressDialog(EachFriend.this);

        intent = getIntent();
        EMAIL = intent.getStringExtra("EMAIL");
        NOTE = intent.getStringExtra("NOTE");
        MYEMAIL = intent.getStringExtra("MYEMAIL");
        PWORD = intent.getStringExtra("PWORD");

        note = (TextView)findViewById(R.id.textView_note);
        email = (TextView)findViewById(R.id.textView_email);
        note.setText(NOTE);
        email.setText("邮箱："+EMAIL);

        send = (Button)findViewById(R.id.button_send);
        delete = (Button)findViewById(R.id.button_delete);
        button_changenote = (Button)findViewById(R.id.button_changenote);
        send.setOnClickListener(this);
        delete.setOnClickListener(this);
        button_changenote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send:
                intent.setClass(EachFriend.this,Chat.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_delete:
                deleteDialog();
                break;
            case R.id.button_changenote:
                addfriend();
                break;
        }
    }

    public void addfriend(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EachFriend.this);
        // 创建一个view，将布局加入view中
        View view = LayoutInflater.from(EachFriend.this).inflate(
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
                    Toast.makeText(EachFriend.this,"输入好友备注",Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    progressDialog.setMessage("处理中...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ChangeNote changeNote = new ChangeNote();
                            String response = changeNote.changeNote(MYEMAIL,PWORD,EMAIL,edt.getText().toString());
                            Message message=new Message();
                            message.what=1;
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


    public void deleteDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(EachFriend.this);
        dialog.setMessage("确认删除好友");
        dialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除好友操作
                        dialog.dismiss();
                        progressDialog = new ProgressDialog(EachFriend.this);
                        progressDialog.setMessage("处理中...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DeleteFriend friend = new DeleteFriend();
                                String response = friend.deleteFriend(MYEMAIL,PWORD,EMAIL,"Delete");
                                Message message=new Message();
                                message.what=1;
                                message.obj=response;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private ChatSQLiteHelper helper;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String response = (String)msg.obj;
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    if(!response.equals("false")) {
                        Toast.makeText(EachFriend.this,"成功",Toast.LENGTH_SHORT).show();
                        String t = MYEMAIL;
                        t = t.replace("@","");
                        t = t.replace(".","");
                        t = t+".db";
                        helper = new ChatSQLiteHelper(EachFriend.this,t);
                        helper.deleteAll();

                        intent.setClass(EachFriend.this,MainInterface.class);
                        intent.putExtra("UNAME",MYEMAIL);
                        intent.putExtra("PWORD",PWORD);
                        intent.putExtra("ALLFRIENDS",response);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(EachFriend.this,"失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
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
