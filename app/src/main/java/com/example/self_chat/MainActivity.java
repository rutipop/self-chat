package com.example.self_chat;

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

public class MainActivity extends AppCompatActivity  implements MessageRecyclerUtils.MessageClickCallback {

    private MessageRecyclerUtils.MessageAddapter addapter = new MessageRecyclerUtils.MessageAddapter();
    private ArrayList<Message> all_messages;


    CollectionReference cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all_messages = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(addapter);
        addapter.callback =this;


        cr = FirebaseFirestore.getInstance().collection("Messages");

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult()!= null ){

                    for (DocumentSnapshot document : task.getResult()) {
                        Message msg = document.toObject(Message.class);
                        all_messages.add(msg);
                    }
                    addapter.submitList(all_messages);
                } else {
                    load_all_messages();
                    addapter.submitList(all_messages);

                }
            }});

        Log.i("number of messages",  ""+all_messages.size());


        Button sendButton = (Button) findViewById(R.id.buttonToSend);
        final EditText filledInSend = (EditText) findViewById(R.id.wantToSend) ;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curr_sent_message = filledInSend.getText().toString();
                if(curr_sent_message.equals("")){

                    Toast.makeText(MainActivity.this, "you can't send an empty message", Toast.LENGTH_LONG).show();

                }
                else{
                    Message msg =new Message(curr_sent_message);
                    all_messages.add(msg);
                    addapter.submitList(all_messages);
                    cr.document(String.valueOf(msg.id)).set(msg);

                }
                filledInSend.setText("");
                save_all_messages();
            }
        });

        if(savedInstanceState != null){
            all_messages =  savedInstanceState.getParcelableArrayList("list_of_messages");
            addapter.submitList(all_messages);

        }
    }

    private void save_all_messages(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(all_messages);
        editor.putString("saved_messages",json);
        editor.apply();
    }


    private void load_all_messages(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json =sp.getString("saved_messages",null);
        Type type = new TypeToken<ArrayList<Message>>(){}.getType();
        all_messages = gson.fromJson(json,type);
        if (all_messages == null){
            all_messages = new ArrayList<>();
        }


    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list_of_messages", all_messages);


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
                ArrayList < Message > peopleCopy = new ArrayList<>(all_messages);
                peopleCopy.remove(message);
                all_messages = peopleCopy;
                addapter.submitList(all_messages);
                save_all_messages();

                cr.document(String.valueOf(message.id)).delete();

            }
        });
        builder.show();

    }
}
