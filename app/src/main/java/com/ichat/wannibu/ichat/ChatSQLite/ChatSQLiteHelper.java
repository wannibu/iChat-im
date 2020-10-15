package com.ichat.wannibu.ichat.ChatSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class ChatSQLiteHelper extends SQLiteOpenHelper {
    private static ChatSQLiteHelper helper;
    private SQLiteDatabase db;

    public ChatSQLiteHelper(Context context, String table) {
        super(context, table, null, 1);
    }

    public static ChatSQLiteHelper getInstance(final Context context, String table) {
        if (helper == null) {
            synchronized (ChatSQLiteHelper.class) {
                if (helper == null) {
                    helper = new ChatSQLiteHelper(context,table);
                }
            }
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS chat (id varchar(64), message varchar(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(ContentValues values){
        db = getWritableDatabase();
        return db.insert("chat",null,values);
    }

    public void  query(List<PersonChat> personChats, String myemail, String email){
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from chat",null);

        while(c.moveToNext()){
            PersonChat personChat = new PersonChat();
            String receive = c.getString(0);
            String[] receives = receive.split("&&");
            //不是我发的消息 就放左边，是我发的 就放右边， 不是聊天中两人的消息就忽略
            if(receives[0].equals(email)&&receives[1].equals(myemail)){
                personChat.setMeSend(false);
                personChat.setChatMessage(c.getString(1));
                personChats.add(personChat);
            }
            if(receives[1].equals(email)&&receives[0].equals(myemail)){
                personChat.setMeSend(true);
                personChat.setChatMessage(c.getString(1));
                personChats.add(personChat);
            }
        }
    }

    public void deleteAll()
    {
        db = helper.getWritableDatabase();
        db.delete("chat",null,null);
    }
}
