package com.ichat.wannibu.ichat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ichat.wannibu.ichat.Activity.MainInterface.ExitApplication;
import com.ichat.wannibu.ichat.Activity.WLS;
import com.ichat.wannibu.ichat.R;


import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {

    private List<Map<String, Object>> datas;
    private Context context;


    public MyAdapter(List<Map<String, Object>> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.me,null);
        ImageView head = (ImageView)convertView.findViewById(R.id.imageView_head);
        TextView myemail = (TextView)convertView.findViewById(R.id.textView_myemail);
        Button logout = (Button) convertView.findViewById(R.id.button_logout);
        Button changep = (Button) convertView.findViewById(R.id.button_changeP);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WLS.class);
                context.startActivity(intent);
                ExitApplication.getInstance().exit();
            }
        });
        changep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"功能尚未开发",Toast.LENGTH_SHORT).show();
            }
        });
        head.setImageResource((Integer)datas.get(position).get("head"));
        myemail.setText(datas.get(position).get("email").toString());
        logout.setText(datas.get(position).get("logout").toString());
        changep.setText(datas.get(position).get("changep").toString());
        return convertView;
    }

}
