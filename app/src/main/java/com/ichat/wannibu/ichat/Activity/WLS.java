package com.ichat.wannibu.ichat.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ichat.wannibu.ichat.R;

//Welcome Login Signup
public class WLS extends AppCompatActivity implements View.OnClickListener {
    Button login;
    Button signup;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        final Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#001c2b"));
        setContentView(R.layout.activity_wls);
        intent = new Intent();
        login = (Button)findViewById(R.id.button_login);
        signup = (Button)findViewById(R.id.button_signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                login.setEnabled(false);
                intent.setClass(WLS.this,LogIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_signup:
                signup.setEnabled(false);
                intent.setClass(WLS.this,SignUp.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
