package com.microcell.imapp;

public class Message {
    public String from;
    public String to;
    public String message;
    public long time;
    public Message(String from,String to,String message,long time){
        this.from=from;
        this.to=to;
        this.message=message;
        this.time=time;
    }
    @Override
    public String toString() {
        return "User{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}