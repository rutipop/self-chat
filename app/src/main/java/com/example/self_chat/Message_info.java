package com.example.self_chat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Message_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);



        Gson g=new Gson();
        Type type=new TypeToken<Message>(){}.getType();

        String message_to_display ="";
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                message_to_display = extras.getString("selected_message");
            }
        }
        final Message msg = g.fromJson(message_to_display,type);

        Button per_delete = (Button) findViewById(R.id.permanent_delete_button);
        TextView details = (TextView) findViewById(R.id.details);

        if(msg.originDevice!=null){
        String text_to_show ="device type : "+ msg.originDevice+"\n"+
                "time stamp : " + msg.timestamp;
        details.setText(text_to_show);
        }


        per_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySelfChatApp app = (MySelfChatApp) getApplication(); ;
                AppDataManager dataManager = app.manager;
                dataManager.deletetItem(msg);
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                finish();

            }
        });



    }
}
