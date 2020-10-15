package com.ichat.wannibu.ichat.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichat.wannibu.ichat.ChatSQLite.PersonChat;
import com.ichat.wannibu.ichat.R;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<PersonChat> lists;

    public ChatAdapter(Context context, List<PersonChat> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    //得到Item的类型，是对方发过来的消息，还是自己发送出去的
    public int getItemViewType(int position) {
        PersonChat personChat = lists.get(position);

        if (personChat.isMeSend()) {
            // 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            // 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        HolderView holderView = null;
        PersonChat personChat = lists.get(arg0);
        boolean isMeSend = personChat.isMeSend();
        if (holderView == null) {
            holderView = new HolderView();
            if (isMeSend) {
                arg1 = View.inflate(context, R.layout.chat_dialog_right_item,null);
                holderView.chat_me_message = (TextView) arg1.findViewById(R.id.tv_chat_me_message);
                holderView.chat_me_message.setText(personChat.getChatMessage());
                holderView.head = (ImageView)arg1.findViewById(R.id.chat_imag_right);
                holderView.head.setImageResource(R.drawable.user);
            } else {
                arg1 = View.inflate(context, R.layout.chat_dialog_left_item,null);
                holderView.tv_chat_come_message = (TextView) arg1.findViewById(R.id.tv_chat_come_message);
                holderView.tv_chat_come_message.setText(personChat.getChatMessage());
                holderView.head = (ImageView)arg1.findViewById(R.id.chat_imag_left);
                holderView.head.setImageResource(R.drawable.user);
            }
            arg1.setTag(holderView);
        } else {
            holderView = (HolderView) arg1.getTag();
        }
        return arg1;
    }

    class HolderView {
        TextView chat_me_message;
        TextView tv_chat_come_message;
        ImageView head;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
