package com.ichat.wannibu.ichat.Activity.MainInterface;

import android.app.Activity;
import android.app.Application;
import java.util.ArrayList;
import java.util.List;

public class ExitApplication extends Application {
    private List<Activity> activityList = new ArrayList<>();
    private static ExitApplication instance;

    public static ExitApplication getInstance(){
        if(null == instance){
            instance = new ExitApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit(){
        for(Activity activity : activityList){
            activity.finish();
        }
        //强制退出
        System.exit(0);
    }
}
