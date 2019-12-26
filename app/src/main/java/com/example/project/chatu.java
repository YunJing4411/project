package com.example.project;

import java.util.Date;

public class chatu {
    private String Text;
    private String User;
    private long Time;

    public chatu(String text,String user)
    {
        Text=text;
        User=user;

        Time=new Date().getTime();
    }

    public chatu(){


    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }
}
