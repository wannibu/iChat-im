package com.ichat.wannibu.ichat.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ichat.wannibu.ichat.R;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_welcom);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //要执行的操作
                Intent intent = new Intent(Welcome.this,WLS.class);
                startActivity(intent);
                Welcome.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1500);//2秒后执行TimeTask的run方法
    }

}
