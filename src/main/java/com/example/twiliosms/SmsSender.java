package com.example.twiliosms;// Install the Java helper library from twilio.com/docs/libraries/java

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC9690af8b958b876e7248b483b73efe4d";
    public static final String AUTH_TOKEN = "01eba08b13dff728962163d527e59b73";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+12533103320"), // to
                        new PhoneNumber("+12537991414"), // from
                        "Where's Eric?")
                .create();

        System.out.println(message.getSid());
    }
}