package com.example.self_chat;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Message implements Parcelable {

    public String text_in_message;
    public String timestamp;
    public String id;
    public String originDevice;



    Message(){}

    //string constructor:
    Message(String text_in_mes){
        Date date= new Date();
        text_in_message = text_in_mes;
        timestamp =(new Date()).toString();
        id = UUID.randomUUID().toString();

        originDevice = getDeviceName();


    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
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
