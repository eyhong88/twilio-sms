package com.example.twiliosms;

public enum SmsRequestEnum {
    BODY("Body="),
    FROM("From="),
    TO("To=");

    private String value;

    SmsRequestEnum(String value){
        this.value = value;
    }

    String getRequestValue(){
        return value;
    }
}
