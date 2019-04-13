package com.example.self_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MessageRecyclerUtils.MessageAddapter addapter = new MessageRecyclerUtils.MessageAddapter();
    private ArrayList<Message> all_messages = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(addapter);

        addapter.submitList(all_messages);

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
                    all_messages.add(new Message(curr_sent_message));
                    addapter.submitList(all_messages);

                }
                filledInSend.setText("");
            }
        });

        if(savedInstanceState != null){
            all_messages =  savedInstanceState.getParcelableArrayList("list_of_messages");
            addapter.submitList(all_messages);

        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list_of_messages", all_messages);


    }



}
