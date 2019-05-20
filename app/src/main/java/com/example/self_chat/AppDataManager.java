package com.example.self_chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class AppDataManager {
    private Executor executor = Executors.newCachedThreadPool();
    FirebaseFirestore firestore;
    private MutableLiveData<ArrayList<Message>> messagesLiveData = new MutableLiveData<>();

    public AppDataManager(){
        messagesLiveData.setValue(new ArrayList<Message>());

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("my_messages").orderBy("timestamp",Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        // which thread am I in right now? do I know? I DONT KNOW!!!!!
                        // a.f.a.i.k  im working really hard and I'm on the main thread right now!
                        // delegate work to the executor:

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                // here I'm at a BG thread for sure
                                if (task.isSuccessful() && task.getResult() != null) {
                                    ArrayList<Message> allItemsFromFirestore = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        allItemsFromFirestore.add(document.toObject(Message.class));
                                    }
                                    AppDataManager.this.messagesLiveData.postValue(allItemsFromFirestore);
                                }
                            }
                        });
                    }
                });
    }

    public LiveData<ArrayList<Message>> getMessagesLiveData() {
        return messagesLiveData;
    }

    public void insertItem(Message itemToInsert) {
        // first update our local phone - post to the live data
        ArrayList<Message> allItemsNew = new ArrayList<>(messagesLiveData.getValue());
        allItemsNew.add(itemToInsert);
        messagesLiveData.postValue(allItemsNew);

        // than update to remote locations - for example firestore:
        firestore.collection("my_messages").document(itemToInsert.id).set(itemToInsert);

    }



    public void deletetItem(Message itemToDelete) {
        // first update our local phone - post to the live data
        ArrayList<Message> allItemsNew = new ArrayList<>(messagesLiveData.getValue());
        allItemsNew.remove(itemToDelete);
        messagesLiveData.postValue(allItemsNew);

        // than update to remote locations - for example firestore
        firestore.collection("my_messages").document(itemToDelete.id).delete();

    }

}