package com.example.self_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.UUID;

public class UserLogin extends AppCompatActivity {
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

//        MySelfChatApp app = (MySelfChatApp) getApplication();
//        AppDataManager dataManager = app.manager;
        firestore = FirebaseFirestore.getInstance();


        firestore.collection("my_username").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {


                                if (task.isSuccessful() && task.getResult() != null) {


                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        go_to_main_with_name(document.get("user_name").toString());

                                    }

                                }





                    }
                });










        SharedPreferences prefs = getSharedPreferences("is_logged_in", MODE_PRIVATE);
        boolean restoredAns = prefs.getBoolean("is_it?", false);
        String restoreName = prefs.getString("the_username",null);




//        if (restoredAns) {
//            go_to_main_with_name(restoreName);
//        }





        final Button nameButton = (Button) findViewById(R.id.nameButton);
        final Button skipButton = (Button) findViewById(R.id.skipButton);
        final EditText filledInName = (EditText) findViewById(R.id.fillName) ;

        //****************************************************************************************************
        //making the username bottun visible or invisible:
        filledInName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String curr_sent_message = filledInName.getText().toString().trim();
                if(!curr_sent_message.isEmpty()){
                    nameButton.setVisibility(View.VISIBLE);
                }
                else{
                    nameButton.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //****************************************************************************************************





        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO When the user will tap back, they won't be navigated to the "configure name" screen again.
                // TODO Next time the user launches the app, the "configure name" screen will be shown to him\her. 

                go_to_main_with_name("");
            }
        });

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save username and go to main activity and write username on right corner!

                firestore = FirebaseFirestore.getInstance();
                final String filled_username = filledInName.getText().toString().trim();

                firestore.collection("my_username")
                        .document("this is the only user:")
                        .set(new HashMap<String,String>(){{put("user_name",filled_username);}});


                SharedPreferences.Editor editor = getSharedPreferences("is_logged_in", MODE_PRIVATE).edit();
                editor.putBoolean("is_it?", true);
                editor.putString("the_username", filled_username);

                editor.apply();

                go_to_main_with_name(filled_username);




            }
        });









    }


    public void go_to_main_with_name(String given_username){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("entered_username",given_username);
        setResult(RESULT_OK,intent);
        startActivity(intent);
        finish();
    }

    public void skip_to_main_activity(){
//        TextView user_name_diaplay =(TextView) findViewById(R.id.user_name_text);
//        user_name_diaplay.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
