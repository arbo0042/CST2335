package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MessageEntered extends AppCompatActivity {

    private boolean sendMessage;
    private String messageEntered;

    public MessageEntered(){
    }

    public MessageEntered(boolean sendMessage, String messageEntered){
        this.sendMessage = sendMessage;
        this.messageEntered = messageEntered;
    }

    public boolean isSendButtonPressed(){
        return sendMessage;
        }

    public String messageEntered(){
        return messageEntered;
    }
}
