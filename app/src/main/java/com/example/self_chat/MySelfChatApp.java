package com.example.self_chat;
import android.app.Application;

public class MySelfChatApp  extends Application{
    public AppDataManager manager;

    @Override
    public void onCreate(){
        super.onCreate();
        manager = new AppDataManager();
        //remember that this function is blocking,meaning that nothing else will happen until you
        // return from it. Do not perform long-operation init there!

    }
}
