package com.example.twiliosms;// Install the Java helper library from twilio.com/docs/libraries/java

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "-";
    public static final String AUTH_TOKEN = "-";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+12533103320"), // to
                        new PhoneNumber("+12537991414"), // from
                        "Where's Eric?")
                .create();

        System.out.println(message.getSid());
    }
}
