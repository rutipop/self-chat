package com.example.self_chat;
import android.app.Application;

public class MySelfChatApp  extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        //remember that this function is blocking,meaning that nothing else will happen until you
        // return from it. Do not perform long-operation init there!

    }
}
