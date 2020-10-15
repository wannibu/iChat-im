package com.ichat.wannibu.ichat.ChatSQLite;

public class PersonChat {

    private String name;
    private String chatMessage;
    private boolean isMeSend;    //是否为本人发送

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getChatMessage() {
        return chatMessage;
    }
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
    public boolean isMeSend() {
        return isMeSend;
    }
    public void setMeSend(boolean isMeSend) {
        this.isMeSend = isMeSend;
    }
    public PersonChat() {
        super();
    }
}
