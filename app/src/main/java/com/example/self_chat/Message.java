package com.example.self_chat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Objects;

public class Message implements Parcelable {

    public String text_in_message;

//    static ArrayList<Message> get_all(){
//        ArrayList<Message> all = new ArrayList<>();
//        return all;
//
//    }






    //string constructor:
    Message(String text_in_mes){
        text_in_message = text_in_mes;
    }

    protected Message(Parcel in) {
        text_in_message = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public boolean equals( Object obj) {
        if(this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass() ) return false;
        return this.text_in_message.equals(((Message) obj).text_in_message);
    }


    @Override
    public int hashCode() {
        return Objects.hash(text_in_message);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text_in_message);
    }





}
