package com.ichat.wannibu.ichat.Adapter;

import com.ichat.wannibu.ichat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendManage {
    static String[] friends;
    public FriendManage(String[] friends) {
        this.friends = friends;
    }

    public List<Map<String,Object>> getFriends(){

        List<Map<String,Object>>dataList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<friends.length;i++){
            Map<String,Object> item = makeMap(friends[i]);
            dataList.add(item);
        }
        return dataList;
    }

    private Map<String,Object> makeMap(String friend){
        Map<String,Object> it = new HashMap<String, Object>();
        it.put("image", R.mipmap.o9 );
        it.put("friend",friend);
        return it;
    }
}
