package com.example.self_chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  implements MessageRecyclerUtils.MessageClickCallback {

    private MessageRecyclerUtils.MessageAddapter addapter = new MessageRecyclerUtils.MessageAddapter();

    MySelfChatApp app ;
    AppDataManager dataManager ;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(addapter);
        addapter.callback =this;

        app = (MySelfChatApp) getApplication();
        dataManager = app.manager;


        final LiveData<ArrayList<Message>> liveData = dataManager.getMessagesLiveData();

        liveData.observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> singleItems) {
                if (singleItems == null) return;

                // here we are in the main thread and we can do whatever we with the UI:

                Button sendButton = (Button) findViewById(R.id.buttonToSend);
                final EditText filledInSend = (EditText) findViewById(R.id.wantToSend) ;

                addapter.submitList(liveData.getValue());
                Log.i("number of messages",  ""+liveData.getValue().size());


                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String curr_sent_message = filledInSend.getText().toString();
                        if(curr_sent_message.equals("")){

                            Toast.makeText(MainActivity.this, "you can't send an empty message", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Message msg =new Message(curr_sent_message);
                            dataManager.insertItem(msg);
                        }
                        filledInSend.setText("");
                    }
                });
            }
        });



    }


    @Override
    public void onMessageClick(final Message message) {


        //creating a dialog:
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("You pressed a message which means you want to delete it");
        builder.setMessage("Are You Sure?");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataManager.deletetItem(message);
            }
        });
        builder.show();

    }
}
