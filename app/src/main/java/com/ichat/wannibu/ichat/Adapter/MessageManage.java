package com.ichat.wannibu.ichat.Adapter;

import com.ichat.wannibu.ichat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManage {
    static String[] messageuname;
    static String[] newmessage;
    public MessageManage(String[] messageuname,String[] newmessage) {
        this.messageuname = messageuname;
        this.newmessage = newmessage;
    }

    public List<Map<String,Object>> getMessage(){

        List<Map<String,Object>>dataList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<messageuname.length;i++){
            Map<String,Object> item = makeMap(messageuname[i],newmessage[i]);
            dataList.add(item);
        }
        return dataList;
    }

    private Map<String,Object> makeMap(String messageuname,String newmessage){
        Map<String,Object> it = new HashMap<String, Object>();
        it.put("messagehead", R.drawable.user );
        it.put("messageuname",messageuname);
        it.put("newmessage",newmessage);
        return it;
    }
}
